package com.emotionalcart.shipment.infra.redis;

import com.emotionalcart.shipment.infra.redis.dto.RedisEvent;
import com.emotionalcart.shipment.infra.redis.dto.RedisMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscribeListener implements MessageListener {

    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            log.info("received message : {}", message);

            RedisMessage messageDto = objectMapper.readValue(message.getBody(), RedisMessage.class);

            log.info("Received message from topic [{}]: {}", messageDto.getSender(), message);

            // Spring 이벤트 발행
            eventPublisher.publishEvent(new RedisEvent(messageDto.getSender(), messageDto.getMessage()));

        } catch (Exception e) {
            log.error("exception :: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
