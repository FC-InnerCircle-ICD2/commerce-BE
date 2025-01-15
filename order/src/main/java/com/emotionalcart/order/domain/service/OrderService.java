package com.emotionalcart.order.domain.service;

import com.emotionalcart.order.domain.dto.OrderInfoResponse;
import com.emotionalcart.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * 주문 정보 조회
     *
     * @param orderId
     * @return
     */
    public OrderInfoResponse getOrderInfo(Long orderId) {
        return OrderInfoResponse.from(orderRepository.findByOrderId(orderId));
    }

}
