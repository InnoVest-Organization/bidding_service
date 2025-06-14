package com.InnoVest.biddingService.service.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.InnoVest.biddingService.dto.kafka.BidSelectedKafkaMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, BidSelectedKafkaMessage> kafkaTemplate;
    
    private static final String BID_SELECTED_TOPIC = "bid-selected";
    
    public void sendBidSelectedMessage(BidSelectedKafkaMessage message) {
        log.info("Sending bid selected message to Kafka for Order ID: {}", message.getOrderId());
        
        CompletableFuture<SendResult<String, BidSelectedKafkaMessage>> future = 
            kafkaTemplate.send(BID_SELECTED_TOPIC, message);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully to topic {}: {}", 
                         BID_SELECTED_TOPIC, message);
            } else {
                log.error("Failed to send message to Kafka: {}", ex.getMessage(), ex);
            }
        });
    }
}
