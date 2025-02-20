package com.emotionalcart.common.security;

import com.emotionalcart.common.jwt.JwtAccessDeniedHandler;
import com.emotionalcart.common.jwt.JwtAuthenticationTokenFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class DefaultSecurityConfig {

    private final AppProperties appProperties;
    private final CustomAuthenticationEntryPoint entryPoint;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(c -> c.configurationSource(corsConfigurationSource()))
            .exceptionHandling(handleException())
            .headers(handleExceptionHeader())
            .sessionManagement(handleSessionPolicy())
            .authorizeHttpRequests(request ->
                                       getAuthorizedUrl(request).permitAll()
                                           .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static Customizer<SessionManagementConfigurer<HttpSecurity>> handleSessionPolicy() {
        return session -> session.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS);
    }

    private static Customizer<HeadersConfigurer<HttpSecurity>> handleExceptionHeader() {
        return config -> config.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
    }

    private Customizer<ExceptionHandlingConfigurer<HttpSecurity>> handleException() {
        return handle -> handle.accessDeniedHandler(jwtAccessDeniedHandler).authenticationEntryPoint(entryPoint);
    }

    private AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl getAuthorizedUrl(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request) {
        return request.requestMatchers(getPermissionUrl());
    }

    protected String[] getPermissionUrl() {
        return new String[] {"/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/actuator/**"};
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> allowDomains = appProperties.getAllowDomains();
        config.setAllowedOrigins(allowDomains);
        config.setAllowedMethods(List.of("GET", "PUT", "DELETE", "POST", "PATCH", "OPTIONS", "HEAD"));
        config.setExposedHeaders(List.of("Access-Control-Allow-Headers",
                                         "Access-Token",
                                         "Refresh-Token",
                                         "Access-Control-Allow-Origin",
                                         "strict-origin-when-cross-origin"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
