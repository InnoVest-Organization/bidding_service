package com.InnoVest.biddingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BiddingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiddingServiceApplication.class, args);
	}

}
