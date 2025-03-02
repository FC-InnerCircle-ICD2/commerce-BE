package com.emotionalcart.shipment.presentation.controller;

import com.emotionalcart.shipment.presentation.controller.request.OrderShipmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "배송", description = "배송 관리 API")
public interface OrderShipmentApiDocs {

    @Operation(summary = "배송 요청 API")
    ResponseEntity<Boolean> createShipment(@Valid @RequestBody OrderShipmentRequest orderShipmentRequest);

}
