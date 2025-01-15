package com.emotionalcart.order.domain.repository;

import com.emotionalcart.order.domain.dto.OrderInfo;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.infrastructure.JpaOrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public OrderInfo findByOrderId(Long orderId) {
        Orders order = jpaOrderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        return OrderInfo.fromEntity(order);
    }

}
