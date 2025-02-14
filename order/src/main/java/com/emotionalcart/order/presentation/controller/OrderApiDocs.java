package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.presentation.controller.request.CreateOrderRequest;
import com.emotionalcart.order.presentation.controller.response.CreatedOrderResponse;
import com.emotionalcart.order.presentation.controller.response.GetOrderListResponse;
import com.emotionalcart.order.presentation.controller.response.OrderDetailResponse;
import com.emotionalcart.order.presentation.controller.response.UserOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "주문", description = "주문 API")
public interface OrderApiDocs {

    @Operation(summary = "주문하기 API")
    ResponseEntity<CreatedOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request);

    @Operation(summary = "주문조회 API")
    ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId);

    @Operation(summary = "사용자 주문 목록 조회 API")
    ResponseEntity<Page<UserOrderResponse>> getOrderList(Long userId, Pageable request);

}
