package com.emotionalcart.core.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi openAPI() {
        return GroupedOpenApi.builder()
                .group("Group API")
                .addOpenApiCustomizer(openAPI -> openAPI.info(apiInfo()).servers(servers())).build();
    }

    private List<Server> servers() {
        return List.of(
                new Server().url("https://product-api.emmotional-cart.click").description("Production Server")
        );
    }

    private Info apiInfo() {
        return new Info()
                .title("Product API")
                .description("상품 도메인 API Docs");
    }

}
