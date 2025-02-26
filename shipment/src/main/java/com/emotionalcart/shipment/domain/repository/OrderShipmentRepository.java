package com.emotionalcart.shipment.domain.repository;

import com.emotionalcart.shipment.domain.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderShipmentRepository extends JpaRepository<Shipment, Long> {

    Page<Shipment> findByProviderId(String providerId, Pageable pageable);

}