package com.InnoVest.biddingService.repository;

import com.InnoVest.biddingService.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Integer> {
    Optional<Bid> findByInventionIdAndInvestorId(Integer inventionId, Integer investorId);
}