package com.InnoVest.biddingService.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceBidRequestDTO {
    @NotNull(message = "Invention ID is required")
    private Integer inventionId;

    @NotNull(message = "Investor ID is required")
    private Integer investorId;

    @NotNull(message = "Bid amount is required")
    private Integer bidAmount;

    @Min(value = 0, message = "Equity must be between 0 and 100")
    @Max(value = 100, message = "Equity must be between 0 and 100")
    private Integer equity = 0;
}
