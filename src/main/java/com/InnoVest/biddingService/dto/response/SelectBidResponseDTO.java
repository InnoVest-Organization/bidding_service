package com.InnoVest.biddingService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectBidResponseDTO {
    private Integer orderId;
    private Integer inventionId;
    private Integer investorId;
    private Boolean selected;
    private String message;
}
