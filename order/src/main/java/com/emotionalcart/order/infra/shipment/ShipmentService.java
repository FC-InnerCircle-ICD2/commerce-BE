package com.emotionalcart.order.infra.shipment;

import com.emotionalcart.order.infra.redis.RedisPublisherService;
import com.emotionalcart.order.infra.redis.dto.RedisMessage;
import com.emotionalcart.order.infra.shipment.dto.OrderShipmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final RedisPublisherService redisPublisherService;

    /**
     * 배송 요청
     */
    public void createShipment(OrderShipmentRequest orderShipmentRequest) {
        String topic = "create-shipment";
        redisPublisherService.pubMsgChannel(topic, RedisMessage.convert(topic, orderShipmentRequest));
    }

}