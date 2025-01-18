package com.emotionalcart.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (REST API를 위한 경우)
                .csrf(AbstractHttpConfigurer::disable)

                // 세션을 Stateless로 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 권한 및 인증 설정
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll() // 모든 요청 인증 없이 접근 허용
//                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
//                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                );


        return http.build();
    }
}
