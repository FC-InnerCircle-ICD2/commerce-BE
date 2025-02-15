package com.emotionalcart.shipment.presentation.controller;

import com.emotionalcart.shipment.application.service.OrderShipmentService;
import com.emotionalcart.shipment.presentation.controller.request.OrderShipmentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 업체 별 배송 조회
     *
     * @param providerId
     * @return
     */
    @GetMapping("/{providerId}")
    public ResponseEntity<Page<OrderShipmentResponse>> getShipmentByProvider(@PathVariable String providerId, Pageable pageable) {
        return ResponseEntity.ok().body(orderShipmentService.getShipmentByProvider(providerId, pageable));
    }

}
