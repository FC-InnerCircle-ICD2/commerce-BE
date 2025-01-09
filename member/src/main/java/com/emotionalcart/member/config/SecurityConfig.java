package com.emotionalcart.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (REST API를 위한 경우)
                .csrf(AbstractHttpConfigurer::disable)

                // 권한 및 인증 설정
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청 인증 없이 접근 허용
//                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
//                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )

                // OAuth2 소셜 로그인 활성화
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // 커스텀 로그인 페이지 URL
                        .defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL
                        .failureUrl("/login?error") // 로그인 실패 시 이동할 URL
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL
                        .invalidateHttpSession(true) // 세션 초기화
                );

        return http.build();
    }
}
