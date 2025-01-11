package com.emotionalcart.order.infra.order;

import com.emotionalcart.order.domain.entity.OrderItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemHistoryRepository extends JpaRepository<OrderItemHistory, Long> {

}
