package com.emotionalcart.order.infra.redis;

import com.emotionalcart.order.infra.redis.dto.RedisMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, RedisMessage> template;

    /**
     * publish
     */
    public void publish(ChannelTopic topic, RedisMessage value) {
        template.convertAndSend(topic.getTopic(), value);
    }

}
