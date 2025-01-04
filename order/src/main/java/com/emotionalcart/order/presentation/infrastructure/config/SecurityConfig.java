package com.emotionalcart.order.presentation.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/product/**")
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/product/**").permitAll()
            .anyRequest().authenticated()
        );

        return http.build();
    }


}
