package com.emotionalcart.shipment.domain.repository;

import com.emotionalcart.shipment.domain.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderShipmentRepository extends JpaRepository<Shipment, Long> {

    Page<Shipment> findByProviderId(Long providerId, Pageable pageable);

    List<Shipment> findByOrderId(Long orderId);

}