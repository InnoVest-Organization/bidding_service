package com.InnoVest.biddingService.controller.publicEP;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.InnoVest.biddingService.dto.request.PlaceBidRequestDTO;
import com.InnoVest.biddingService.dto.request.SelectBidRequestDTO;
import com.InnoVest.biddingService.dto.response.GetInventionBidsResponseDTO;
import com.InnoVest.biddingService.dto.response.PlaceBidResponseDTO;
import com.InnoVest.biddingService.dto.response.SelectBidResponseDTO;
import com.InnoVest.biddingService.service.impl.BidService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@Tag(name = "Bid Controller", description = "APIs for managing bids")
public class BidController {

    private final BidService bidService;

    @PostMapping
    @Operation(summary = "Place a new bid", 
                description = "This endpoint allows an investor to place a new bid on an invention.\n" +
                    "If the investor has already placed a bid on the same invention, the existing bid will be updated (overridden) with the new bid details.")
    public ResponseEntity<PlaceBidResponseDTO> placeBid(@Valid @RequestBody PlaceBidRequestDTO bidRequestDTO) {
        PlaceBidResponseDTO response = bidService.placeBid(bidRequestDTO);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/invention/{inventionId}")
    @Operation(summary = "Get all bids for an invention", 
               description = "This endpoint returns all bids that have been placed on a specific invention.\n" + 
                    "Even if the list is empty, we return a valid response with the inventionId and an empty list (not null)")
    public ResponseEntity<GetInventionBidsResponseDTO> getInventionBids(@PathVariable Integer inventionId) {
        GetInventionBidsResponseDTO response = bidService.getInventionBids(inventionId);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/select")
    @Operation(summary = "Select or deselect a bid", 
               description = "This endpoint allows updating the selection status of a bid. " +
                    "Set selected=true to select the bid or selected=false to deselect it.")
    public ResponseEntity<SelectBidResponseDTO> selectBid(@Valid @RequestBody SelectBidRequestDTO requestDTO) {
        SelectBidResponseDTO response = bidService.selectBid(requestDTO);
        return ResponseEntity.ok(response);
    }
}
