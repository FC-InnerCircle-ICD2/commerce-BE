package com.emotionalcart.order.infra.redis;

import com.emotionalcart.order.infra.redis.dto.RedisMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisherService {

    private final RedisPublisher redisPublisher;

    /**
     * 레디스 퍼블리싱
     *
     * @param channel
     * @param message
     */
    public void pubMsgChannel(String channel, RedisMessage message) {
        redisPublisher.publish(ChannelTopic.of(channel), message);
    }

}
