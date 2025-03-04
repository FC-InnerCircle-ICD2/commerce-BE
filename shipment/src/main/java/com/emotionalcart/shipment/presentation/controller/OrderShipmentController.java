package com.emotionalcart.shipment.presentation.controller;

import com.emotionalcart.common.jwt.JwtAuthentication;
import com.emotionalcart.shipment.application.service.OrderShipmentService;
import com.emotionalcart.shipment.presentation.controller.request.OrderShipmentRequest;
import com.emotionalcart.shipment.presentation.controller.request.ShipmentUpdateRequest;
import com.emotionalcart.shipment.presentation.controller.response.OrderShipmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Boolean> createShipment(@Valid @RequestBody OrderShipmentRequest orderShipmentRequest) {
        Boolean isSuccessful = orderShipmentService.createShipment(orderShipmentRequest.mapToDomain());
        return ResponseEntity.ok().body(isSuccessful);
    }

    /**
     * 관리자/업체 별 배송 조회
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<OrderShipmentResponse>> getShipmentByProvider(@AuthenticationPrincipal JwtAuthentication jwt,
                                                                             Pageable pageable) {
        return ResponseEntity.ok().body(orderShipmentService.getShipmentByProvider(jwt, pageable));
    }

    /**
     * 관리자/업체 별 배송 수정
     *
     * @param jwt
     * @param shipmentId
     * @param request
     * @return
     */
    @PatchMapping("/{shipmentId}")
    public ResponseEntity<Boolean> updateShipmentStatus(@AuthenticationPrincipal JwtAuthentication jwt
        , @PathVariable Long shipmentId, @RequestBody ShipmentUpdateRequest request) {

        return ResponseEntity.ok().body(orderShipmentService.updateShipmentStatus(jwt, shipmentId, request));
    }

}
