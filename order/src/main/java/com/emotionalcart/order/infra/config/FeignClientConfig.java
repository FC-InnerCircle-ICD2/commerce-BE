package com.emotionalcart.order.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public GlobalFeignErrorDecoder globalFeignErrorDecoder() {
        return new GlobalFeignErrorDecoder();
    }

}
