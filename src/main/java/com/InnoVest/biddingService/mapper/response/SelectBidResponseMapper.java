package com.InnoVest.biddingService.mapper.response;

import com.InnoVest.biddingService.dto.response.SelectBidResponseDTO;
import com.InnoVest.biddingService.model.Bid;

public class SelectBidResponseMapper {
    public static SelectBidResponseDTO toResponseDTO(Bid bid) {
        return SelectBidResponseDTO.builder()
                .orderId(bid.getOrderId())
                .inventionId(bid.getInventionId())
                .investorId(bid.getInvestorId())
                .selected(bid.getSelected())
                .message(bid.getSelected() ? "Bid selected successfully" : "Bid unselected successfully")
                .build();
    }
}
