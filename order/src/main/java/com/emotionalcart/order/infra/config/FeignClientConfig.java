package com.emotionalcart.order.infra.config;

import com.emotionalcart.common.jwt.JwtHeaderValidator;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FeignClientConfig {

    private final JwtHeaderValidator jwtHeaderValidator;

    @Bean
    public GlobalFeignErrorDecoder globalFeignErrorDecoder() {
        return new GlobalFeignErrorDecoder();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", getToken());
    }

    private String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("Request attributes are not available. Ensure this method is called in a web request context.");
        }

        return jwtHeaderValidator.obtainAuthorizationToken(attributes.getRequest())
                .orElseThrow(() -> {
                    log.error("JWT processing failed: Authorization token is missing.");
                    return new IllegalStateException("Authorization token is required but missing.");
                });
    }

}
