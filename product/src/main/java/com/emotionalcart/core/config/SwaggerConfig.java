package com.emotionalcart.core.config;

import com.emotionalcart.common.swagger.SwaggerConfigProperties;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    private final SwaggerConfigProperties swaggerConfigProperties;

    @Bean
    public GroupedOpenApi openAPI() {
        return GroupedOpenApi.builder()
            .group("Group API")
            .addOpenApiCustomizer(openAPI -> openAPI.info(apiInfo()).servers(servers())).build();
    }

    private List<Server> servers() {
        return swaggerConfigProperties.getServers().stream()
            .map(config -> new Server().url(config.getUrl()).description(config.getDescription()))
            .toList();
    }

    private Info apiInfo() {
        return new Info()
            .title("Product API")
            .description("상품 도메인 API Docs");
    }

}
