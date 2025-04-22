package com.InnoVest.biddingService.controller;

import com.InnoVest.biddingService.dto.BiddingDTO;
import com.InnoVest.biddingService.service.BiddingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BiddingController {

    @Autowired
    private BiddingService biddingService;

    @PostMapping
    public ResponseEntity<BiddingDTO> placeBid(@Valid @RequestBody BiddingDTO biddingDTO) {
        return ResponseEntity.ok(biddingService.placeBid(biddingDTO));
    }

    @GetMapping("/{inventionId}")
    public ResponseEntity<List<BiddingDTO>> getBidsForInvention(@PathVariable Integer inventionId) {
        return ResponseEntity.ok(biddingService.getBidsForInvention(inventionId));
    }
}
