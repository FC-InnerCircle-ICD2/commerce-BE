package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.CreateOrder;
import lombok.RequiredArgsConstructor;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RedissonMultiLockProvider {

    private final RedissonClient redissonClient;

    RedissonMultiLock getRedissonMultiLock(CreateOrder createOrder) {
        List<RLock> locks = getLocksByOrderItems(createOrder);
        return new RedissonMultiLock(locks.toArray(new RLock[0]));
    }

    private List<RLock> getLocksByOrderItems(CreateOrder createOrder) {
        return createOrder.getOrderItems().stream().map(item -> redissonClient.getLock("product_lock:" + item.getProductId())).toList();
    }

}
