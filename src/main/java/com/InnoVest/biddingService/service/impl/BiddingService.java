package com.InnoVest.biddingService.service;

import com.InnoVest.biddingService.dto.BiddingDTO;
import com.InnoVest.biddingService.entity.Bidding;
import com.InnoVest.biddingService.repository.BiddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BiddingService {

    @Autowired
    private BiddingRepository biddingRepository;

    @Autowired
    private ModelMapper modelMapper;

    public BiddingDTO placeBid(BiddingDTO biddingDTO) {
        Bidding bidding = modelMapper.map(biddingDTO, Bidding.class);
        bidding.setSelected(false);  // Default to not selected
        Bidding savedBidding = biddingRepository.save(bidding);
        return modelMapper.map(savedBidding, BiddingDTO.class);
    }

    public List<BiddingDTO> getBidsForInvention(Integer inventionId) {
        return biddingRepository.findByInventionId(inventionId).stream()
                .map(bid -> modelMapper.map(bid, BiddingDTO.class))
                .collect(Collectors.toList());
    }
}
