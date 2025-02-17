package com.emotionalcart.common.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties("app")
public class AppProperties {

    private List<String> allowDomains;
    
}
