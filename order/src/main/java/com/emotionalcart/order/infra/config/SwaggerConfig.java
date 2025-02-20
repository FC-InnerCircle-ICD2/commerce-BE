package com.emotionalcart.order.infra.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi openAPI() {
        return GroupedOpenApi.builder()
                .group("Group API")
                .addOpenApiCustomizer(openAPI -> openAPI.info(apiInfo())).build();
    }

    private Info apiInfo() {
        return new Info()
                .title("Order API")
                .description("주문 도메인 API Docs");
    }
}
