package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.application.CreateOrderService;
import com.emotionalcart.order.application.service.GetOrderService;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.dto.GetOrder;
import com.emotionalcart.order.presentation.controller.request.CreateOrderRequest;
import com.emotionalcart.order.presentation.controller.response.CreatedOrderResponse;
import com.emotionalcart.order.presentation.controller.response.GetOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderService createOrderService;

    private final GetOrderService getOrderService;

    @PostMapping
    public ResponseEntity<CreatedOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        CreatedOrder createdOrder = createOrderService.createOrder(request.mapToDomain());
        // 주문 생성
        return ResponseEntity.ok(CreatedOrderResponse.from(createdOrder));
    }

    /**
     * 주문 조회
     *
     * @param orderId 주문번호
     * @return
     */
    @GetMapping
    public ResponseEntity<GetOrderResponse> getOrder(@RequestParam Long orderId) {
        GetOrder getOrder = getOrderService.getOrder(orderId);
        return ResponseEntity.ok(GetOrderResponse.from(getOrder));
    }

}
