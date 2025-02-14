package com.emotionalcart.adminorder.domain.repository;

import com.emotionalcart.order.domain.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 관리자 에서 사용하는 주문 레포지토리
 */
public interface AdminOrderRepository extends JpaRepository<Orders, Long> {

}
