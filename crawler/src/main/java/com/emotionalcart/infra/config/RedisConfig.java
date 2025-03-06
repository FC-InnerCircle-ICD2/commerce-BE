package com.emotionalcart.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfig {

    @Value("${redis.stand-alone.host}")
    private String standAloneHost;

    @Value("${redis.stand-alone.port}")
    private String standAlonePort;

    /**
     * RedisConnectionFactory 빈을 생성
     *
     * @return RedisConnectionFactory 빈
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(
            new RedisStandaloneConfiguration(standAloneHost, Integer.parseInt(standAlonePort)));
    }

    /**
     * RedisConnectionFactory를 사용하여 JedisPooled 빈을 생성
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @return JedisPooled 빈
     */
    @Bean
    public JedisPooled jedisPooled(RedisConnectionFactory redisConnectionFactory) {
        JedisConnectionFactory jedisConnectionFactory =
            (JedisConnectionFactory)redisConnectionFactory;
        return new JedisPooled(
            jedisConnectionFactory.getPoolConfig(),
            jedisConnectionFactory.getHostName(),
            jedisConnectionFactory.getPort());
    }

}
