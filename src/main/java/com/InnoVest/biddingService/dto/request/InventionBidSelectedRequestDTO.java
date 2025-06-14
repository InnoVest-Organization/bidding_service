package com.InnoVest.biddingService.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventionBidSelectedRequestDTO {
    @JsonProperty("Invention_ID")
    private Integer inventionId;
    
    @JsonProperty("Investor_ID")
    private Integer investorId;
    
    @JsonProperty("Is_Live")
    private Boolean isLive;
}
