package com.emotionalcart.shipment.domain.repository;

import com.emotionalcart.shipment.domain.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderShipmentRepository extends JpaRepository<Shipment, Long> {

}
