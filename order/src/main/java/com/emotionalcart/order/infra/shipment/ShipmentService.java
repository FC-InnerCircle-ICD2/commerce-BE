package com.emotionalcart.order.infra.shipment;

import com.emotionalcart.order.infra.shipment.dto.OrderShipmentRequest;
import com.emotionalcart.order.infra.shipment.http.ShipmentFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentFeignClient shipmentFeignClient;

    /**
     * 배송 요청
     */
    public Boolean createShipment(OrderShipmentRequest orderShipmentRequest) {
        ResponseEntity<Boolean> shipment = shipmentFeignClient.createShipment(orderShipmentRequest);
        return shipment.getBody();
    }

}
