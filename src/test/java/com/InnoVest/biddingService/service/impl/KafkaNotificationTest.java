package com.InnoVest.biddingService.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.InnoVest.biddingService.dto.kafka.BidSelectedKafkaMessage;
import com.InnoVest.biddingService.dto.request.SelectBidRequestDTO;
import com.InnoVest.biddingService.dto.response.InvestorEmailResponseDTO;
import com.InnoVest.biddingService.dto.response.SelectBidResponseDTO;
import com.InnoVest.biddingService.model.Bid;
import com.InnoVest.biddingService.repository.BidRepository;

@ExtendWith(MockitoExtension.class)
class KafkaNotificationTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private KafkaService kafkaService;

    @InjectMocks
    private BidService bidService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bidService, "inventionServiceUrl", "http://localhost:5002");
        ReflectionTestUtils.setField(bidService, "investorServiceUrl", "http://localhost:5006");
    }

    @Test
    void selectBid_whenBidIsSelected_shouldSendKafkaMessage() {
        // Arrange
        Integer orderId = 1;
        Integer inventionId = 4000;
        Integer investorId = 203;
        Integer bidAmount = 50000;
        Integer equity = 15;
        
        Bid bid = new Bid();
        bid.setOrderId(orderId);
        bid.setInventionId(inventionId);
        bid.setInvestorId(investorId);
        bid.setBidAmount(bidAmount);
        bid.setEquity(equity);
        bid.setSelected(false);
        
        SelectBidRequestDTO requestDTO = new SelectBidRequestDTO();
        requestDTO.setOrderId(orderId);
        requestDTO.setSelected(true);
        
        when(bidRepository.findById(orderId)).thenReturn(Optional.of(bid));
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);
        
        // Mock successful response from invention service
        ResponseEntity<String> successResponse = new ResponseEntity<>("Bid selection updated successfully!", HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(successResponse);
        
        // Mock successful response from investor service
        InvestorEmailResponseDTO emailResponseDTO = new InvestorEmailResponseDTO("john@example.com");
        ResponseEntity<InvestorEmailResponseDTO> emailResponse = new ResponseEntity<>(emailResponseDTO, HttpStatus.OK);
        when(restTemplate.exchange(
                eq("http://localhost:5006/api/investors/203/email"),
                eq(HttpMethod.GET),
                any(),
                eq(InvestorEmailResponseDTO.class)
        )).thenReturn(emailResponse);
        
        // Act
        SelectBidResponseDTO responseDTO = bidService.selectBid(requestDTO);
        
        // Assert - basic response assertions
        assertNotNull(responseDTO);
        assertEquals(orderId, responseDTO.getOrderId());
        assertEquals(inventionId, responseDTO.getInventionId());
        assertEquals(investorId, responseDTO.getInvestorId());
        assertEquals(true, responseDTO.getSelected());
        
        // Verify bid was updated
        ArgumentCaptor<Bid> bidCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepository).save(bidCaptor.capture());
        assertEquals(true, bidCaptor.getValue().getSelected());
        
        // Verify invention service was called
        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:5002/api/inventions/bidSelected"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );
        
        // Verify investor service was called
        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:5006/api/investors/203/email"),
                eq(HttpMethod.GET),
                any(),
                eq(InvestorEmailResponseDTO.class)
        );
        
        // Verify Kafka message was sent
        ArgumentCaptor<BidSelectedKafkaMessage> kafkaMessageCaptor = ArgumentCaptor.forClass(BidSelectedKafkaMessage.class);
        verify(kafkaService, times(1)).sendBidSelectedMessage(kafkaMessageCaptor.capture());
        
        // Verify Kafka message content
        BidSelectedKafkaMessage capturedMessage = kafkaMessageCaptor.getValue();
        assertEquals("john@example.com", capturedMessage.getEmail());
        assertEquals(orderId, capturedMessage.getOrderId());
        assertEquals(inventionId, capturedMessage.getInventionId());
        assertEquals(investorId, capturedMessage.getInvestorId());
        assertEquals(bidAmount, capturedMessage.getBidAmount());
        assertEquals(equity, capturedMessage.getEquity());
    }
}
