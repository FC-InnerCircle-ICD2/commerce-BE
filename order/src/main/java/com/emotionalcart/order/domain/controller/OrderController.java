package com.emotionalcart.order.domain.controller;

import com.emotionalcart.order.domain.dto.OrderInfoResponse;
import com.emotionalcart.order.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 정보 API
     *
     * @param orderId 주문 번호
     * @return
     */
    @GetMapping
    public ResponseEntity<OrderInfoResponse> getOrderInfo(@RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.getOrderInfo(orderId));
    }

}
