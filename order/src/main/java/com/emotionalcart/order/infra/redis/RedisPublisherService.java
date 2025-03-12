package com.emotionalcart.order.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisherService {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher;
    private final RedisListenerService redisSubscribeListener;

    /**
     * 레디스 퍼블리싱
     *
     * @param channel
     * @param message
     * @param <T>
     */
    public <T> void pubMsgChannel(String channel, T message) {

        redisMessageListenerContainer.addMessageListener(redisSubscribeListener, ChannelTopic.of(channel));

        redisPublisher.publish(ChannelTopic.of(channel), message);
    }

}
