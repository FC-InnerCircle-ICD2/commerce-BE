package com.emotionalcart.order.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "jasypt.encryptor")
public class JasyptProperties {

    private String password;

}
