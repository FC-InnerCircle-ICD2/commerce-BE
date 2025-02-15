package com.emotionalcart.shipment.application.service;

import com.emotionalcart.shipment.domain.dto.CreateShipment;
import com.emotionalcart.shipment.domain.entity.Shipment;
import com.emotionalcart.shipment.domain.enums.ShipmentStatus;
import com.emotionalcart.shipment.domain.repository.OrderShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderShipmentService {

    private final OrderShipmentRepository orderShipmentRepository;

    /**
     * 배송 요청 메소드
     *
     * @param createShipment 배송 요청 값
     * @return
     */
    public void createShipment(CreateShipment createShipment) {
        List<Shipment> shipmentList = createShipment.getProviderIds().stream().map(providerId -> Shipment.of(providerId,
                                                                                                             createShipment.getOrderId(),
                                                                                                             ShipmentStatus.SHIP_REQUESTED,
                                                                                                             createShipment.getName(),
                                                                                                             createShipment.getPhoneNumber(),
                                                                                                             createShipment.getAddress(),
                                                                                                             createShipment.getDetailAddress(),
                                                                                                             createShipment.getZoneCode(),
                                                                                                             createShipment.getDeliveryMemo())).toList();

        orderShipmentRepository.saveAll(shipmentList);
    }

}
