package com.emotionalcart.shipment.application.service;

import com.emotionalcart.common.jwt.JwtAuthentication;
import com.emotionalcart.common.jwt.MemberRole;
import com.emotionalcart.shipment.domain.converter.ShipmentStatusConverter;
import com.emotionalcart.shipment.domain.dto.CreateShipment;
import com.emotionalcart.shipment.domain.entity.Shipment;
import com.emotionalcart.shipment.domain.enums.ShipmentStatus;
import com.emotionalcart.shipment.domain.repository.OrderShipmentRepository;
import com.emotionalcart.shipment.infra.repository.provider.ProviderRepository;
import com.emotionalcart.shipment.infra.repository.provider.entity.Provider;
import com.emotionalcart.shipment.presentation.controller.request.ShipmentUpdateRequest;
import com.emotionalcart.shipment.presentation.controller.response.OrderShipmentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderShipmentService {

    private final OrderShipmentRepository orderShipmentRepository;
    private final ProviderRepository providerRepository;
    private final ShipmentStatusConverter shipmentStatusConverter;

    /**
     * 배송 요청 메소드
     *
     * @param createShipment 배송 요청 값
     * @return
     */
    @Transactional
    public Boolean createShipment(CreateShipment createShipment) {
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
        log.info("successfully request shipment");
        return Boolean.TRUE;
    }

    /**
     * 업체 별 배송 목록 조회
     *
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<OrderShipmentResponse> getShipmentByProvider(JwtAuthentication jwt, Pageable pageable) {
        Set<MemberRole> roles = jwt.roles();
        Page<Shipment> shipmentPage = Page.empty();
        if (roles.contains(MemberRole.ADMIN_MEMBER)) {
            shipmentPage = orderShipmentRepository.findAll(pageable);

        } else if (roles.contains(MemberRole.PROVIDER_MEMBER)) {
            Long id = jwt.id();
            Provider provider = providerRepository.findByMemberId(id).orElseThrow(() -> new AuthorizationDeniedException("등록된 업체가 없습니다."));
            shipmentPage = orderShipmentRepository.findByProviderId(provider.getId(), pageable);
        }

        return shipmentPage.map(s -> OrderShipmentResponse.of(s.getId(), s.getOrderId(),
                                                              s.getStatus().getDescription(),
                                                              s.getTrackingNumber(),
                                                              s.getShippedAt(),
                                                              s.getDeliveredAt()));
    }

    /**
     * 배송 상태 수정 기능
     *
     * @param jwt
     * @param shipmentId
     * @param request
     * @return
     */
    @Transactional
    public Boolean updateShipmentStatus(JwtAuthentication jwt, Long shipmentId, ShipmentUpdateRequest request) {
        if (Boolean.FALSE.equals(isUpdateStatus(jwt))) {
            return Boolean.FALSE;
        }

        Shipment shipment = orderShipmentRepository.findById(shipmentId)
            .orElseThrow(() -> new IllegalArgumentException("배송 정보를 찾을 수 없습니다. ID: " + shipmentId));

        ShipmentStatus shipmentStatus = ShipmentStatusConverter.convert(request.getStatus());
        shipment.updateStatus(shipmentStatus);
        updateOrderStatus(shipment.getOrderId());
        return Boolean.TRUE;
    }

    private void updateOrderStatus(Long orderId) {

        List<Shipment> orderList = orderShipmentRepository.findByOrderId(orderId);

        Set<ShipmentStatus> shipmentStatuses = orderList.stream()
            .map(Shipment::getStatus).collect(Collectors.toSet());
        
    }

    /**
     * 수정 가능한 상태인지 확인
     *
     * @param jwt
     * @return
     */
    private Boolean isUpdateStatus(JwtAuthentication jwt) {
        Set<MemberRole> roles = jwt.roles();
        return roles.contains(MemberRole.PROVIDER_MEMBER) || roles.contains(MemberRole.ADMIN_MEMBER);
    }

}