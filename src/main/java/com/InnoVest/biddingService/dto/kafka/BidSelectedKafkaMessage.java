package com.InnoVest.biddingService.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidSelectedKafkaMessage {
    private String email;
    private Integer orderId;
    private Integer inventionId;
    private Integer investorId;
    private Integer bidAmount;
    private Integer equity;
}
