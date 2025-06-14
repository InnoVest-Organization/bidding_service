package com.InnoVest.biddingService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(5000);    // 5 seconds
        
        RestTemplate restTemplate = new RestTemplate(factory);
        
        // Add logging for requests
        restTemplate.getInterceptors().add((request, body, execution) -> {
            log.debug("Making request to: {}", request.getURI());
            return execution.execute(request, body);
        });
        
        return restTemplate;
    }
}
