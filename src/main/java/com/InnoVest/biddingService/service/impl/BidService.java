package com.InnoVest.biddingService.service.impl;

import com.InnoVest.biddingService.dto.request.PlaceBidRequestDTO;
import com.InnoVest.biddingService.dto.response.GetInventionBidsResponseDTO;
import com.InnoVest.biddingService.dto.response.PlaceBidResponseDTO;
import com.InnoVest.biddingService.mapper.response.GetInventionBidsResponseMapper;
import com.InnoVest.biddingService.mapper.response.PlaceBidResponseMapper;
import com.InnoVest.biddingService.model.Bid;
import com.InnoVest.biddingService.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;

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
}
