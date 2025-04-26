package com.InnoVest.biddingService.mapper.request;

import com.InnoVest.biddingService.dto.request.PlaceBidRequestDTO;
import com.InnoVest.biddingService.model.Bid;

public class PlaceBidRequestMapper {

    public static Bid toEntity(PlaceBidRequestDTO dto) {
        Bid bid = new Bid();
        bid.setInventionId(dto.getInventionId());
        bid.setInvestorId(dto.getInvestorId());
        bid.setBidAmount(dto.getBidAmount());
        bid.setEquity(dto.getEquity() != null ? dto.getEquity() : 0);
        bid.setSelected(false);
        return bid;
    }
}
