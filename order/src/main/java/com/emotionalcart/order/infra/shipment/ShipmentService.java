package com.emotionalcart.order.infra.shipment;

import com.emotionalcart.order.infra.shipment.dto.OrderShipmentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String STREAM_KEY = "shipment:request:stream";
    private final ObjectMapper objectMapper;

    public void createShipment(OrderShipmentRequest orderShipmentRequest) {
        try {
            String json = objectMapper.writeValueAsString(orderShipmentRequest);
            Map<String, String> map = Map.of("shipmentOrderRequest", json);

            MapRecord<String, String, String> record = MapRecord.create(STREAM_KEY, map);
            redisTemplate.opsForStream().add(record);

        } catch (JsonProcessingException e) {
            log.error("error : {}", e.getMessage());
        }
    }

}