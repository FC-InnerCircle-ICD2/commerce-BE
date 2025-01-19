package com.emotionalcart.order.application.service;

import com.emotionalcart.order.domain.dto.OrderDetail;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import com.emotionalcart.order.infra.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderDetail getOrderDetail(Long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new InvalidValueRequestException("일치하는 주문번호를 찾을 수 없습니다."));
        log.error("일치하는 주문번호를 찾을 수 없습니다 : {}", orderId);
        return OrderDetail.from(orders);
    }

}
