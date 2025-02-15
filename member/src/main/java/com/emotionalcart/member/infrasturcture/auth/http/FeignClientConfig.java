package com.emotionalcart.member.infrasturcture.auth.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Bean
    public GlobalFeignErrorDecoder globalFeignErrorDecoder() {
        return new GlobalFeignErrorDecoder();
    }

}
