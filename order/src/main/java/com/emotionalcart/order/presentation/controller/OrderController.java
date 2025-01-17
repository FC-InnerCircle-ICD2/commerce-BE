package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.application.CreateOrderService;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.presentation.controller.request.CreateOrderRequest;
import com.emotionalcart.order.presentation.controller.response.CreatedOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderService createOrderService;

    @PostMapping
    public ResponseEntity<CreatedOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        CreatedOrder createdOrder = createOrderService.createOrder(request.mapToDomain());
        // 주문 생성
        return ResponseEntity.ok(CreatedOrderResponse.from(createdOrder));
    }

}
