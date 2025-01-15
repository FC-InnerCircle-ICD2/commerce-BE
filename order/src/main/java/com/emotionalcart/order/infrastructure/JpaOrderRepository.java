package com.emotionalcart.order.infrastructure;

import com.emotionalcart.order.domain.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaOrderRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findById(Long id);

}
