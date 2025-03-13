package com.emotionalcart.shipment.domain.entity;

import com.emotionalcart.shipment.domain.enums.ShipmentStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusTimestamp {

    ShipmentStatus value();

}
