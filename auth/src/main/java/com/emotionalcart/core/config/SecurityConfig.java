package com.emotionalcart.core.config;

import com.emotionalcart.auth.application.CustomOAuth2UserService;
import com.emotionalcart.auth.presentation.handler.CustomSuccessHandler;
import com.emotionalcart.common.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (REST API를 위한 경우)
            .csrf(AbstractHttpConfigurer::disable)

            //From 로그인 방식 disable
            .formLogin(AbstractHttpConfigurer::disable)

            //HTTP Basic 인증 방식 disable
            .httpBasic(AbstractHttpConfigurer::disable)

            //oauth2
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler)
            )

            //경로별 인가 작업(권한 및 인증 설정
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/v1/auth/uri/**", "/oauth2/authorization/**").permitAll()
//                .anyRequest().authenticated())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
            )
            // 세션을 Stateless 설정
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

}