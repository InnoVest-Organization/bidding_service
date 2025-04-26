package com.InnoVest.biddingService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetInventionBidsResponseDTO {
    private Integer inventionId;
    private List<BidDTO> bids;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BidDTO {
        private Integer orderId;
        private Integer investorId;
        private Integer bidAmount;
        private Integer equity;
        private Boolean selected;
    }
}