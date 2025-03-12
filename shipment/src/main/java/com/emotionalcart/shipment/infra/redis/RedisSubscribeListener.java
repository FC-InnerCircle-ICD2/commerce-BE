package com.emotionalcart.shipment.infra.redis;

import com.emotionalcart.shipment.infra.redis.dto.RedisMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscribeListener implements MessageListener {

    private final RedisTemplate<String, RedisMessage> template;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            String publishMessage = template
                .getStringSerializer().deserialize(message.getBody());

            RedisMessage messageDto = objectMapper.readValue(publishMessage, RedisMessage.class);

            log.info("Redis Subscribe Channel : " + messageDto.getRoomId());
            log.info("Redis SUB Message : {}", publishMessage);
        } catch (Exception e) {
            log.error("exception :: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
