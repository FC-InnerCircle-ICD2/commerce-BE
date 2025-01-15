package com.emotionalcart.order.domain.repository;

import com.emotionalcart.order.domain.dto.OrderInfo;

public interface OrderRepository {

    OrderInfo findByOrderId(Long id);

}
