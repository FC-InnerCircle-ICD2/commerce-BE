package com.emotionalcart.order.infra.shipment;

import com.emotionalcart.order.infra.redis.dto.RedisMessage;
import com.emotionalcart.order.infra.shipment.dto.OrderShipmentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final RedisTemplate<String, RedisMessage> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String STREAM_KEY = "shipment_stream"; // Stream 이름

    public void createShipment(OrderShipmentRequest orderShipmentRequest) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(RedisMessage.convert(STREAM_KEY, orderShipmentRequest));
            redisTemplate.opsForStream().add(STREAM_KEY, Map.of("data", jsonMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}