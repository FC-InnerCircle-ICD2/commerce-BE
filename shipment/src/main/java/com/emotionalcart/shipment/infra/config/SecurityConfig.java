package com.emotionalcart.shipment.infra.config;

import com.emotionalcart.common.jwt.JwtAccessDeniedHandler;
import com.emotionalcart.common.jwt.JwtAuthenticationTokenFilter;
import com.emotionalcart.common.security.AppProperties;
import com.emotionalcart.common.security.CustomAuthenticationEntryPoint;
import com.emotionalcart.common.security.DefaultSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends DefaultSecurityConfig {

    public SecurityConfig(AppProperties appProperties,
                          CustomAuthenticationEntryPoint entryPoint,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        super(appProperties, entryPoint, jwtAuthenticationTokenFilter, jwtAccessDeniedHandler);
    }

}
