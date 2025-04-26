package com.InnoVest.biddingService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI biddingServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("InnoVest Bidding Service API")
                        .description("API documentation for Bidding Service where investors can place bids on inventions.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thisara Weerakoon")
                                .email("thisara.21@cse.mrt.ac.lk")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                );
    }
}
