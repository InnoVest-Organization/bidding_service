package com.InnoVest.biddingService.service.impl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.InnoVest.biddingService.dto.request.InventionBidSelectedRequestDTO;
import com.InnoVest.biddingService.dto.request.SelectBidRequestDTO;
import com.InnoVest.biddingService.dto.response.SelectBidResponseDTO;
import com.InnoVest.biddingService.model.Bid;
import com.InnoVest.biddingService.repository.BidRepository;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BidService bidService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bidService, "inventionServiceUrl", "http://localhost:5002");
    }

    @Test
    void selectBid_whenBidIsSelected_shouldNotifyInventionService() {
        // Arrange
        Integer orderId = 1;
        Integer inventionId = 4000;
        Integer investorId = 203;
        
        Bid bid = new Bid();
        bid.setOrderId(orderId);
        bid.setInventionId(inventionId);
        bid.setInvestorId(investorId);
        bid.setSelected(false);
        
        SelectBidRequestDTO requestDTO = new SelectBidRequestDTO();
        requestDTO.setOrderId(orderId);
        requestDTO.setSelected(true);
        
        when(bidRepository.findById(orderId)).thenReturn(Optional.of(bid));
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);
        
        // Mock successful response from invention service
        ResponseEntity<String> successResponse = new ResponseEntity<>("Bid selection updated successfully!", HttpStatus.OK);
        when(restTemplate.exchange(
                eq("http://localhost:5002/api/inventions/bidSelected"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(successResponse);
        
        // Act
        SelectBidResponseDTO responseDTO = bidService.selectBid(requestDTO);
        
        // Assert
        assertNotNull(responseDTO);
        assertEquals(orderId, responseDTO.getOrderId());
        assertEquals(inventionId, responseDTO.getInventionId());
        assertEquals(investorId, responseDTO.getInvestorId());
        assertEquals(true, responseDTO.getSelected());
        
        // Verify bid was updated
        ArgumentCaptor<Bid> bidCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepository).save(bidCaptor.capture());
        assertEquals(true, bidCaptor.getValue().getSelected());
        
        // Verify invention service was called with POST
        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:5002/api/inventions/bidSelected"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );
    }
    
    @Test
    void selectBid_whenBidIsDeselected_shouldNotNotifyInventionService() {
        // Arrange
        Integer orderId = 1;
        Integer inventionId = 4000;
        Integer investorId = 203;
        
        Bid bid = new Bid();
        bid.setOrderId(orderId);
        bid.setInventionId(inventionId);
        bid.setInvestorId(investorId);
        bid.setSelected(true);
        
        SelectBidRequestDTO requestDTO = new SelectBidRequestDTO();
        requestDTO.setOrderId(orderId);
        requestDTO.setSelected(false);
        
        when(bidRepository.findById(orderId)).thenReturn(Optional.of(bid));
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);
        
        // Act
        SelectBidResponseDTO responseDTO = bidService.selectBid(requestDTO);
        
        // Assert
        assertNotNull(responseDTO);
        assertEquals(orderId, responseDTO.getOrderId());
        assertEquals(inventionId, responseDTO.getInventionId());
        assertEquals(investorId, responseDTO.getInvestorId());
        assertEquals(false, responseDTO.getSelected());
        
        // Verify bid was updated
        ArgumentCaptor<Bid> bidCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepository).save(bidCaptor.capture());
        assertEquals(false, bidCaptor.getValue().getSelected());
        
        // Verify invention service was NOT called
        verify(restTemplate, times(0)).exchange(
                anyString(),
                any(HttpMethod.class),
                any(),
                eq(String.class)
        );
    }
    
    @Test
    void selectBid_whenInventionServiceErrors_shouldContinueProcessing() {
        // Arrange
        Integer orderId = 1;
        Integer inventionId = 4000;
        Integer investorId = 203;
        
        Bid bid = new Bid();
        bid.setOrderId(orderId);
        bid.setInventionId(inventionId);
        bid.setInvestorId(investorId);
        bid.setSelected(false);
        
        SelectBidRequestDTO requestDTO = new SelectBidRequestDTO();
        requestDTO.setOrderId(orderId);
        requestDTO.setSelected(true);
        
        when(bidRepository.findById(orderId)).thenReturn(Optional.of(bid));
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);
        
        // Mock error response from invention service
        when(restTemplate.exchange(
                eq("http://localhost:5002/api/inventions/bidSelected"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new org.springframework.web.client.ResourceAccessException("Connection refused"));
        
        // Act
        SelectBidResponseDTO responseDTO = bidService.selectBid(requestDTO);
        
        // Assert - should still return successful response even if invention service call fails
        assertNotNull(responseDTO);
        assertEquals(orderId, responseDTO.getOrderId());
        assertEquals(inventionId, responseDTO.getInventionId());
        assertEquals(investorId, responseDTO.getInvestorId());
        assertEquals(true, responseDTO.getSelected());
        
        // Verify bid was updated regardless of invention service error
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
    }
    
    @Test
    void selectBid_shouldSendCorrectJsonPropertyNames() {
        // Arrange
        Integer orderId = 1;
        Integer inventionId = 4000;
        Integer investorId = 203;
        
        Bid bid = new Bid();
        bid.setOrderId(orderId);
        bid.setInventionId(inventionId);
        bid.setInvestorId(investorId);
        bid.setSelected(false);
        
        SelectBidRequestDTO requestDTO = new SelectBidRequestDTO();
        requestDTO.setOrderId(orderId);
        requestDTO.setSelected(true);
        
        when(bidRepository.findById(orderId)).thenReturn(Optional.of(bid));
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);
        
        ResponseEntity<String> successResponse = new ResponseEntity<>("Bid selection updated successfully!", HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(successResponse);
        
        // Act
        bidService.selectBid(requestDTO);
        
        // Assert - Capture the HttpEntity to verify JSON payload
        ArgumentCaptor<HttpEntity<?>> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                entityCaptor.capture(),
                eq(String.class)
        );
        
        // Extract and verify the payload
        HttpEntity<?> capturedEntity = entityCaptor.getValue();
        InventionBidSelectedRequestDTO capturedPayload = (InventionBidSelectedRequestDTO) capturedEntity.getBody();
        
        // Verify the values
        assertNotNull(capturedPayload);
        assertEquals(inventionId, capturedPayload.getInventionId());
        assertEquals(investorId, capturedPayload.getInvestorId());
        assertEquals(false, capturedPayload.getIsLive());
    }
}
