package com.emotionalcart.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "coupang")
public class CoupangCrawlerProperties {

    private String baseUrl;

    private String category;

    private String defaultCategory;

    private String defaultCategoryName;

    private String defaultCategoryUrl;

}
