package com.emotionalcart.stock.infra;

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
                .title("Stock API")
                .description("재고 도메인 API Docs");
    }
}
