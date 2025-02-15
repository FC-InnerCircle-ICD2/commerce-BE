package com.emotionalcart.core.config;

import com.emotionalcart.auth.application.CustomOAuth2UserService;
import com.emotionalcart.auth.presentation.handler.CustomAuthenticationEntryPoint;
import com.emotionalcart.core.config.jwt.JwtFilter;
import com.emotionalcart.core.config.jwt.JwtUtil;
import com.emotionalcart.auth.presentation.handler.CustomSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (REST API를 위한 경우)
                .csrf(AbstractHttpConfigurer::disable)

                //From 로그인 방식 disable
                .formLogin(AbstractHttpConfigurer::disable)

                //HTTP Basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)

                //경로별 인가 작업(권한 및 인증 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )

                //oauth2
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(customSuccessHandler)
                        .defaultSuccessUrl("/", true)
                )

                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)

                // 세션을 Stateless 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}