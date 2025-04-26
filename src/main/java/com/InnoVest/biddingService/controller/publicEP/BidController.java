package com.InnoVest.biddingService.controller.publicEP;

import com.InnoVest.biddingService.dto.request.PlaceBidRequestDTO;
import com.InnoVest.biddingService.dto.response.PlaceBidResponseDTO;
import com.InnoVest.biddingService.service.impl.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@Tag(name = "Bid Controller", description = "APIs for managing bids")
public class BidController {

    private final BidService bidService;

    @PostMapping
    @Operation(summary = "Place a new bid", description = "This endpoint allows an investor to place a new bid on an invention.\n" +
            "If the investor has already placed a bid on the same invention, the existing bid will be updated (overridden) with the new bid details.")
    public ResponseEntity<PlaceBidResponseDTO> placeBid(@Valid @RequestBody PlaceBidRequestDTO bidRequestDTO) {
        PlaceBidResponseDTO response = bidService.placeBid(bidRequestDTO);
        return ResponseEntity.ok(response);
    }
}
