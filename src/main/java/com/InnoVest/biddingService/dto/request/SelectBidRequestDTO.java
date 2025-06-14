package com.InnoVest.biddingService.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectBidRequestDTO {
    @NotNull(message = "Order ID is required")
    private Integer orderId;
    
    @NotNull(message = "Selected status is required")
    private Boolean selected;
}
