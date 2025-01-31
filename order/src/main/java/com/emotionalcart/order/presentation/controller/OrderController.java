package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.application.service.CreateOrderService;
import com.emotionalcart.order.application.service.OrderDetailService;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.dto.OrderDetail;
import com.emotionalcart.order.presentation.controller.request.CreateOrderRequest;
import com.emotionalcart.order.presentation.controller.response.CreatedOrderResponse;
import com.emotionalcart.order.presentation.controller.response.OrderDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "order", description = "order API")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderService createOrderService;

    private final OrderDetailService orderDetailService;

    @PostMapping
    @Operation(summary = "create Order API")
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
    @GetMapping("/{orderId}")
    @Operation(summary = "get Order API")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId) {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(orderId);
        return ResponseEntity.ok(OrderDetailResponse.from(orderDetail));
    }

}
