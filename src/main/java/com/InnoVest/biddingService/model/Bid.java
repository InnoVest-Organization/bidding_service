package com.InnoVest.biddingService.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bidding_data")
public class Bid {
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
    private Integer equity = 0;

    @Column(name = "Selected", nullable = true)
    private Boolean selected = false;
}