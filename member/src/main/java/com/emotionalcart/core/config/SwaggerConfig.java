package com.emotionalcart.core.config;

import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
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
                .title("Member API")
                .description("회원 도메인 API Docs");
    }
    
}
