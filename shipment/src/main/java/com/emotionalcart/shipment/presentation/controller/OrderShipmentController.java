package com.emotionalcart.shipment.presentation.controller;

import com.emotionalcart.shipment.application.service.OrderShipmentService;
import com.emotionalcart.shipment.presentation.controller.request.OrderShipmentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shipment")
@RequiredArgsConstructor
public class OrderShipmentController implements OrderShipmentApiDocs {

    private final OrderShipmentService orderShipmentService;

    /**
     * 배송 요청 api
     *
     * @param orderShipmentRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> createShipment(@Valid @RequestBody OrderShipmentRequest orderShipmentRequest) {
        orderShipmentService.createShipment(orderShipmentRequest.mapToDomain());
        return ResponseEntity.ok().build();
    }

}
