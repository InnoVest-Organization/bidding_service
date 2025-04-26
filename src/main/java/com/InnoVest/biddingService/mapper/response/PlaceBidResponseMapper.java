package com.InnoVest.biddingService.mapper.response;

import com.InnoVest.biddingService.dto.response.PlaceBidResponseDTO;
import com.InnoVest.biddingService.model.Bid;

public class PlaceBidResponseMapper {

    public static PlaceBidResponseDTO toResponseDTO(Bid bid) {
        return PlaceBidResponseDTO.builder()
                .orderId(bid.getOrderId())
                .investorId(bid.getInvestorId())
                .inventionId(bid.getInventionId())
                .bidAmount(bid.getBidAmount())
                .equity(bid.getEquity())
                .selected(bid.getSelected())
                .build();
    }
}