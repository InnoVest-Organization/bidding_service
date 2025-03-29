package com.InnoVest.biddingService.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BiddingDTO {

    private Long orderId;

    @NotNull(message = "Invention ID is required")
    private Long inventionId;

    @NotNull(message = "Investor ID is required")
    private Long investorId;

    @Min(value = 1, message = "Bid amount must be greater than zero")
    private Integer bidAmount;

    @Min(value = 1, message = "Equity percentage must be greater than zero")
    private Integer equity;

    private Boolean selected;
}
