package com.emotionalcart.order.infrastructure.configuration;

import com.emotionalcart.order.presentation.client.converter.CustomQueryMapEncoder;
import feign.QueryMapEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public QueryMapEncoder queryMapEncoder() {
        return new CustomQueryMapEncoder();
    }

}
