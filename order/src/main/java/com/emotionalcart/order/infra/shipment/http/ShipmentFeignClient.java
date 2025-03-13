package com.emotionalcart.order.infra.shipment.http;

import com.emotionalcart.order.infra.config.FeignClientConfig;
import com.emotionalcart.order.infra.shipment.dto.OrderShipmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shipment-service", url = "${shipment.find.feign-endpoint}", path = "/api/v1/shipment", configuration = FeignClientConfig.class)
public interface ShipmentFeignClient {

    @PostMapping
    ResponseEntity<Boolean> createShipment(@RequestBody OrderShipmentRequest orderShipmentRequest);

}