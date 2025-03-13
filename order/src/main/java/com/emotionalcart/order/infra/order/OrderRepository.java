package com.emotionalcart.order.infra.order;

import com.emotionalcart.order.domain.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Page<Orders> findByOrderMemberId(Long userId, Pageable pageable);

    Optional<Orders> findByIdAndOrderMemberId(Long orderId, Long id);

}
