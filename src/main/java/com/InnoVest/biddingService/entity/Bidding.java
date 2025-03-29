package com.InnoVest.biddingService.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bidding_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bidding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_ID")
    private Integer orderId;

    @Column(name = "Invention_ID", nullable = true)
    private Integer inventionId;

    @Column(name = "Investor_ID", nullable = true)
    private Integer investorId;

    @Column(name = "Bid_Amount", nullable = true)
    private Integer bidAmount;

    @Column(name = "Equity", nullable = true)
    private Integer equity;

    @Column(name = "Selected", nullable = true)
    private Boolean selected;
}
