package com.emotionalcart.order.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher<K, V> {

    private final RedisTemplate<K, V> template;

    /**
     * publish
     */
    public void publish(ChannelTopic topic, V value) {
        template.convertAndSend(topic.getTopic(), value);
    }

}
