package com.emotionalcart.shipment.application.service;

import com.emotionalcart.shipment.domain.dto.CreateShipment;
import com.emotionalcart.shipment.domain.entity.Shipment;
import com.emotionalcart.shipment.domain.enums.ShipmentStatus;
import com.emotionalcart.shipment.domain.repository.OrderShipmentRepository;
import com.emotionalcart.shipment.presentation.controller.response.OrderShipmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 업체 별 배송 목록 조회
     *
     * @param providerId
     * @param pageable
     * @return
     */
    public Page<OrderShipmentResponse> getShipmentByProvider(String providerId, Pageable pageable) {
        Page<Shipment> shipmentPage = orderShipmentRepository.findByProviderId(providerId, pageable);
        return shipmentPage.map(s -> OrderShipmentResponse.of(s.getOrderId(),
                                                              s.getStatus().getDescription(),
                                                              s.getTrackingNumber(),
                                                              s.getShippedAt(),
                                                              s.getDeliveredAt()));
    }

}