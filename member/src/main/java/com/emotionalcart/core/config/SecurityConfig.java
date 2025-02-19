package com.emotionalcart.core.config;

import com.emotionalcart.common.jwt.JwtAccessDeniedHandler;
import com.emotionalcart.common.jwt.JwtAuthenticationTokenFilter;
import com.emotionalcart.common.security.AppProperties;
import com.emotionalcart.common.security.CustomAuthenticationEntryPoint;
import com.emotionalcart.common.security.DefaultSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends DefaultSecurityConfig {

    public SecurityConfig(AppProperties appProperties, CustomAuthenticationEntryPoint entryPoint, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        super(appProperties, entryPoint, jwtAuthenticationTokenFilter, jwtAccessDeniedHandler);
    }

    @Override
    protected String[] getPermissionUrl() {
        return new String[] {"/api/v1/members/auth/**", "/api/v1/admin/members/auth/**", "/error"};
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
