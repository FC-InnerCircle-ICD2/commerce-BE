package com.emotionalcart.core.config;

import com.emotionalcart.auth.application.CustomOAuth2UserService;
import com.emotionalcart.auth.presentation.handler.CustomSuccessHandler;
import com.emotionalcart.auth.presentation.handler.OAuth2AuthorizationRequestCustomizer;
import com.emotionalcart.common.security.AppProperties;
import com.emotionalcart.common.security.CustomAuthenticationEntryPoint;
import com.emotionalcart.core.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppProperties appProperties;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final OAuth2AuthorizationRequestCustomizer authorizationRequestCustomizer;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (REST API를 위한 경우)
            .csrf(AbstractHttpConfigurer::disable)

            //From 로그인 방식 disable
            .formLogin(AbstractHttpConfigurer::disable)

            //HTTP Basic 인증 방식 disable
            .httpBasic(AbstractHttpConfigurer::disable)
            .cors(c -> c.configurationSource(corsConfigurationSource()))

            //oauth2
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpoint -> endpoint
                    .authorizationRequestResolver(authorizationRequestCustomizer)
                )
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .addLogoutHandler(((request, response, authentication) -> {
                    CookieUtil.deleteCookie(response, "Access-Token");
                    CookieUtil.deleteCookie(response, "Refresh-Token");
                }))
                .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
            )
            //경로별 인가 작업(권한 및 인증 설정)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**",
                                 "/v3/api-docs/**",
                                 "/login/oauth2/code/**",
                                 "/swagger-resources/**", "/api/v1/auth/uri/**", "/oauth2/authorization/**", "/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
            )
            // 세션을 Stateless 설정
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        return http.build();
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
                                         "strict-origin-when-cross-origin",
                                         "Set-Cookie"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}