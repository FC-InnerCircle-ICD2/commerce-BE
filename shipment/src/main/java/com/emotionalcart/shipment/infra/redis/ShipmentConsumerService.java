package com.emotionalcart.shipment.infra.redis;

import com.emotionalcart.shipment.application.service.OrderShipmentService;
import com.emotionalcart.shipment.presentation.controller.request.OrderShipmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ShipmentConsumerService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final OrderShipmentService orderShipmentService;
    private static final String STREAM_KEY = "shipment:request:stream";

    public void consumeShipmentEvents() {
        List<MapRecord<String, Object, Object>> messages = redisTemplate
            .opsForStream()
            .read(StreamOffset.create(STREAM_KEY, ReadOffset.from("0")));

        for (MapRecord<String, Object, Object> message : messages) {
            try {
                // JSON 문자열을 객체로 변환
                OrderShipmentRequest orderShipmentRequest =
                    objectMapper.readValue((String)message.getValue().get("shipmentOrderRequest"), OrderShipmentRequest.class);

                log.info("배송 요청 처리 - 주문 ID: " + orderShipmentRequest.getOrderId());
                orderShipmentService.createShipment(orderShipmentRequest.mapToDomain());

                redisTemplate.opsForStream().delete(STREAM_KEY, message.getId());

            } catch (Exception e) {
                log.error("메시지 처리 실패: " + e.getMessage());
            }
        }
    }

}
