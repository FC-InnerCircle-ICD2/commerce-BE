package com.emotionalcart.shipment.infra.redis.dto;

public record RedisEvent(String topic, Object message) {

}
