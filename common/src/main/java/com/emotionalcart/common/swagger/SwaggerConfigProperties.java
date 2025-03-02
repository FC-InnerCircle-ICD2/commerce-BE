package com.emotionalcart.common.swagger;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "swagger-config")
public class SwaggerConfigProperties {

    private List<ServerDetail> servers;

    @Data
    public static class ServerDetail {

        private String url;
        private String description;

    }

}