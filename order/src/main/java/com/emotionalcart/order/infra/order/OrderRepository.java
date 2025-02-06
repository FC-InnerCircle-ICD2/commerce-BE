package com.emotionalcart.order.infra.order;

import com.emotionalcart.order.domain.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

}
