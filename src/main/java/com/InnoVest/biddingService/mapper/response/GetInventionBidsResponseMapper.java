package com.InnoVest.biddingService.mapper.response;

import com.InnoVest.biddingService.dto.response.GetInventionBidsResponseDTO;
import com.InnoVest.biddingService.model.Bid;
import java.util.List;
import java.util.stream.Collectors;

public class GetInventionBidsResponseMapper {

    public static GetInventionBidsResponseDTO toResponseDTO(Integer inventionId, List<Bid> bids) {
        List<GetInventionBidsResponseDTO.BidDTO> bidDTOs = bids.stream()
                .map(GetInventionBidsResponseMapper::toBidDTO)
                .collect(Collectors.toList());

        return GetInventionBidsResponseDTO.builder()
                .inventionId(inventionId)
                .bids(bidDTOs)
                .build();
    }

    private static GetInventionBidsResponseDTO.BidDTO toBidDTO(Bid bid) {
        return GetInventionBidsResponseDTO.BidDTO.builder()
                .orderId(bid.getOrderId())
                .investorId(bid.getInvestorId())
                .bidAmount(bid.getBidAmount())
                .equity(bid.getEquity())
                .selected(bid.getSelected())
                .build();
    }
}