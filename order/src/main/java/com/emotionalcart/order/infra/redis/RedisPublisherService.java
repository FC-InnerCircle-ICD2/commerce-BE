package com.emotionalcart.order.infra.redis;

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
     * @param <T>
     */
    public <T> void pubMsgChannel(String channel, T message) {
        redisPublisher.publish(ChannelTopic.of(channel), message);
    }

}
