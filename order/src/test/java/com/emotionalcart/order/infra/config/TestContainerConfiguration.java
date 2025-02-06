package com.emotionalcart.order.infra.config;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestContainerConfiguration {

    @Bean
    @ServiceConnection(name = "redis")
    public GenericContainer<?> redisContainer() {
        return new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379); // Redis 기본 포트
    }

}
