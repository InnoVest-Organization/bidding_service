package com.InnoVest.biddingService.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.InnoVest.biddingService.dto.kafka.BidSelectedKafkaMessage;
import com.InnoVest.biddingService.dto.request.InventionBidSelectedRequestDTO;
import com.InnoVest.biddingService.dto.request.PlaceBidRequestDTO;
import com.InnoVest.biddingService.dto.request.SelectBidRequestDTO;
import com.InnoVest.biddingService.dto.response.GetInventionBidsResponseDTO;
import com.InnoVest.biddingService.dto.response.InvestorEmailResponseDTO;
import com.InnoVest.biddingService.dto.response.PlaceBidResponseDTO;
import com.InnoVest.biddingService.dto.response.SelectBidResponseDTO;
import com.InnoVest.biddingService.mapper.response.GetInventionBidsResponseMapper;
import com.InnoVest.biddingService.mapper.response.PlaceBidResponseMapper;
import com.InnoVest.biddingService.mapper.response.SelectBidResponseMapper;
import com.InnoVest.biddingService.model.Bid;
import com.InnoVest.biddingService.repository.BidRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final RestTemplate restTemplate;
    private final KafkaService kafkaService;
    
    @org.springframework.beans.factory.annotation.Value("${invention.service.url}")
    private String inventionServiceUrl;
    
    @org.springframework.beans.factory.annotation.Value("${investor.service.url:http://localhost:5006}")
    private String investorServiceUrl;

    public PlaceBidResponseDTO placeBid(PlaceBidRequestDTO request) {
        log.info("Placing bid for Investor {} on Invention {}", request.getInvestorId(), request.getInventionId());

        Optional<Bid> existingBidOpt = bidRepository.findByInventionIdAndInvestorId(
                request.getInventionId(), request.getInvestorId());

        Bid bid;
        if (existingBidOpt.isPresent()) {
            bid = existingBidOpt.get();
            log.info("Existing bid found. Overriding the existing bid (Order ID: {})", bid.getOrderId());
        } else {
            bid = new Bid();
        }

        bid.setInventionId(request.getInventionId());
        bid.setInvestorId(request.getInvestorId());
        bid.setBidAmount(request.getBidAmount());
        bid.setEquity(request.getEquity() != null ? request.getEquity() : 0);
        bid.setSelected(false);

        Bid savedBid = bidRepository.save(bid);
        return PlaceBidResponseMapper.toResponseDTO(savedBid);
    }
    
    public GetInventionBidsResponseDTO getInventionBids(Integer inventionId) {
        log.info("Retrieving all bids for Invention {}", inventionId);
        
        List<Bid> bids = bidRepository.findAllByInventionId(inventionId);
        log.info("Found {} bids for Invention {}", bids.size(), inventionId);
        
        // Even if the list is empty, we return a valid response with the inventionId
        // and an empty list (not null)
        return GetInventionBidsResponseMapper.toResponseDTO(inventionId, bids);
    }
    
    public SelectBidResponseDTO selectBid(SelectBidRequestDTO request) {
        log.info("Updating selection status for Order ID {}: selected={}", request.getOrderId(), request.getSelected());
        
        Bid bid = bidRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Bid not found with Order ID: " + request.getOrderId()));
        
        // Update the selected field
        bid.setSelected(request.getSelected());
        
        // Save the updated bid
        Bid savedBid = bidRepository.save(bid);
        log.info("Bid selection status updated successfully for Order ID {}", savedBid.getOrderId());
        
        // Make additional service calls when a bid is selected
        if (request.getSelected()) {
            // 1. First notify the invention service
            notifyInventionService(savedBid);
            
            // 2. Get investor email and send Kafka message
            notifyInvestorViaKafka(savedBid);
        }
        
        return SelectBidResponseMapper.toResponseDTO(savedBid);
    }
    
    private void notifyInventionService(Bid bid) {
        try {
            // Create request payload
            InventionBidSelectedRequestDTO inventionRequest = new InventionBidSelectedRequestDTO();
            inventionRequest.setInventionId(bid.getInventionId());
            inventionRequest.setInvestorId(bid.getInvestorId());
            inventionRequest.setIsLive(false);
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<InventionBidSelectedRequestDTO> entity = new HttpEntity<>(inventionRequest, headers);
            
            // Make the API call
            log.info("Notifying invention service about bid selection for invention ID: {}, investor ID: {}",
                     bid.getInventionId(), bid.getInvestorId());
            
            // Using POST instead of PATCH as the server doesn't support PATCH
            ResponseEntity<String> response = restTemplate.exchange(
                inventionServiceUrl + "/api/inventions/bidSelected",
                HttpMethod.POST,
                entity,
                String.class
            );
            
            // Check if the invention service successfully updated the bid selection
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully notified invention service about bid selection: {}", response.getBody());
            } else {
                log.warn("Invention service responded with non-success status: {} - {}", 
                         response.getStatusCode(), response.getBody());
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Client error when notifying invention service: {}, Status code: {}", 
                      e.getMessage(), e.getStatusCode(), e);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("Cannot connect to invention service: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error notifying invention service about bid selection: {}", e.getMessage(), e);
        }
    }
    
    private void notifyInvestorViaKafka(Bid bid) {
        try {
            String investorEmailUrl = investorServiceUrl + "/api/investors/" + bid.getInvestorId() + "/email";
            log.info("Fetching investor email from: {}", investorEmailUrl);
            
            ResponseEntity<InvestorEmailResponseDTO> emailResponse = restTemplate.exchange(
                investorEmailUrl,
                HttpMethod.GET,
                null,
                InvestorEmailResponseDTO.class
            );
            
            if (emailResponse.getStatusCode().is2xxSuccessful() && emailResponse.getBody() != null) {
                String investorEmail = emailResponse.getBody().getEmail();
                log.info("Retrieved investor email: {} for investor ID: {}", investorEmail, bid.getInvestorId());
                
                // Create and send Kafka message
                BidSelectedKafkaMessage kafkaMessage = BidSelectedKafkaMessage.builder()
                    .email(investorEmail)
                    .orderId(bid.getOrderId())
                    .inventionId(bid.getInventionId())
                    .investorId(bid.getInvestorId())
                    .bidAmount(bid.getBidAmount())
                    .equity(bid.getEquity())
                    .build();
                
                kafkaService.sendBidSelectedMessage(kafkaMessage);
            } else {
                log.warn("Failed to retrieve investor email. Status: {}", emailResponse.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error in investor email retrieval or Kafka notification: {}", e.getMessage(), e);
        }
    }
}
