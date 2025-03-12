package com.emotionalcart.shipment.infra.redis.handler;

import com.emotionalcart.shipment.application.service.OrderShipmentService;
import com.emotionalcart.shipment.domain.dto.CreateShipment;
import com.emotionalcart.shipment.infra.redis.dto.RedisEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisEventHandler {

    private final OrderShipmentService orderShipmentService;

    @EventListener
    public void handleShipmentEvent(RedisEvent event) {
        if ("create-shipment".equals(event.topic())) {
            log.info("Handling Shipment Event: {}", event.message());
            orderShipmentService.createShipment((CreateShipment)event.message());
        }
    }

}
