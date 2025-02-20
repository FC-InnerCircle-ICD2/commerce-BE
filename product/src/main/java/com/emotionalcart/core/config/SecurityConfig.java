package com.emotionalcart.core.config;

import com.emotionalcart.common.jwt.JwtAccessDeniedHandler;
import com.emotionalcart.common.jwt.JwtAuthenticationTokenFilter;
import com.emotionalcart.common.security.AppProperties;
import com.emotionalcart.common.security.CustomAuthenticationEntryPoint;
import com.emotionalcart.common.security.DefaultSecurityConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig extends DefaultSecurityConfig {

    public SecurityConfig(AppProperties appProperties,
                          CustomAuthenticationEntryPoint entryPoint,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        super(appProperties, entryPoint, jwtAuthenticationTokenFilter, jwtAccessDeniedHandler);
    }

    @Override
    protected String[] getPermissionUrl() {
        return new String[]{"/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/actuator/**",
                "/api/v1/products/**",};
    }
}
