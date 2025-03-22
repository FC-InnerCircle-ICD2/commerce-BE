package com.emotionalcart.order.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> template;

    /**
     * publish
     */
    public void publish(ChannelTopic topic, Object value) {
        template.convertAndSend(topic.getTopic(), value);
    }

}
