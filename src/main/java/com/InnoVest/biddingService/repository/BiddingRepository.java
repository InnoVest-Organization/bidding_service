package com.InnoVest.biddingService.repository;

import com.InnoVest.biddingService.entity.Bidding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BiddingRepository extends JpaRepository<Bidding, Long> {
    List<Bidding> findByInventionId(Integer inventionId);
}