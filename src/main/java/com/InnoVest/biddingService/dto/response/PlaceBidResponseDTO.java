package com.InnoVest.biddingService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceBidResponseDTO {
    private Integer orderId;
    private Integer inventionId;
    private Integer investorId;
    private Integer bidAmount;
    private Integer equity;
    private Boolean selected;
}