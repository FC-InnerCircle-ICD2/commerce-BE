package com.emotionalcart.shipment.domain.converter;

import com.emotionalcart.shipment.domain.enums.ShipmentStatus;
import com.emotionalcart.shipment.presentation.controller.request.ShipmentUpdateRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * order 배송 상태와 변환 시켜주는 convertor
 */
@Getter
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShipmentStatusConverter {

    private static final Map<ShipmentUpdateRequest.ShipmentUpdateStatus, ShipmentStatus> statusMap =
        new EnumMap<>(ShipmentUpdateRequest.ShipmentUpdateStatus.class);

    static {
        statusMap.put(ShipmentUpdateRequest.ShipmentUpdateStatus.SHIP_REQUESTED, ShipmentStatus.SHIP_REQUESTED);
        statusMap.put(ShipmentUpdateRequest.ShipmentUpdateStatus.PENDING, ShipmentStatus.PENDING);
        statusMap.put(ShipmentUpdateRequest.ShipmentUpdateStatus.SHIPPED, ShipmentStatus.SHIPPED);
        statusMap.put(ShipmentUpdateRequest.ShipmentUpdateStatus.DELIVERING, ShipmentStatus.DELIVERING);
        statusMap.put(ShipmentUpdateRequest.ShipmentUpdateStatus.OUT_FOR_DELIVERY, ShipmentStatus.OUT_FOR_DELIVERY);
        statusMap.put(ShipmentUpdateRequest.ShipmentUpdateStatus.DELIVERED, ShipmentStatus.DELIVERED);
        statusMap.put(ShipmentUpdateRequest.ShipmentUpdateStatus.CANCELLED, ShipmentStatus.CANCELLED);
    }

    public static ShipmentStatus convert(ShipmentUpdateRequest.ShipmentUpdateStatus updateStatus) {
        return statusMap.getOrDefault(updateStatus, ShipmentStatus.SHIP_REQUESTED);
    }

}
