package com.emotionalcart.shipment.domain.entity;

import com.emotionalcart.shipment.domain.enums.ShipmentStatus;
import jakarta.persistence.PreUpdate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 배송 상태 값 처리하는 리스너
 */
public class StatusTimestampListener {

    @PreUpdate
    public void onPreUpdate(Object entity) {
        try {
            Field statusField = entity.getClass().getDeclaredField("status");
            statusField.setAccessible(true);
            ShipmentStatus status = (ShipmentStatus)statusField.get(entity);

            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(StatusTimestamp.class)) {
                    StatusTimestamp annotation = field.getAnnotation(StatusTimestamp.class);
                    if (annotation.value() == status) {
                        field.setAccessible(true);
                        field.set(entity, LocalDateTime.now());
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error processing @StatusTimestamp", e);
        }
    }

}
