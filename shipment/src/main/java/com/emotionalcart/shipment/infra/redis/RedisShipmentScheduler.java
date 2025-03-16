package com.emotionalcart.shipment.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisShipmentScheduler {

    private final ShipmentConsumerService shipmentConsumerService;

    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    public void processShipments() {
        shipmentConsumerService.consumeShipmentEvents();
    }

}
