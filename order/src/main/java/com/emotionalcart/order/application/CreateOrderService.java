package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.CardInfo;
import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.infra.advice.exceptions.RedissonLockException;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.payment.PaymentInfo;
import com.emotionalcart.order.infra.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final RedissonMultiLockProvider redissonMultiLockProvider;

    /**
     * 주문 생성
     *
     * @param createOrder 주문 생성 정보
     * @return 생성된 주문
     */
    @Transactional
    public CreatedOrder createOrder(CreateOrder createOrder) {
        createOrder.valid();
        log.info("createOrder: {}", createOrder);
        Orders orders = Orders.createOrder(createOrder);
        orderRepository.save(orders);
        log.info("created order.id: {}", orders.getId());
        RedissonMultiLock multiLock = redissonMultiLockProvider.getRedissonMultiLock(createOrder);
        try {
            if (multiLock.tryLock(10, 10, TimeUnit.SECONDS)) {
                // TODO 상품 재고 조회
                for (CreateOrderItem orderItem : createOrder.getOrderItems()) {

                }
                // TODO 결제 서비스 호출
                payment(orders, createOrder.getCardInfo());
                shipment(orders);
                orders.addHistory();
                // TODO 상품 재고 차감
            } else {
                throw new RedissonLockException("잠금 획득 실패: 다른 사용자가 처리 중입니다.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RedissonLockException(e.getMessage());
        } finally {
            multiLock.unlock();
        }
        return CreatedOrder.from(orders);
    }

    private void payment(Orders orders, CardInfo cardInfo) {
        log.info("request payment orders.id: {}, cardInfo {}", orders.getId(), cardInfo);
        paymentService.pay(PaymentInfo.create());
        orders.requestPayment();
    }

    private void shipment(Orders orders) {
        log.info("request shipment orders.id: {}", orders.getId());
        // TODO 배송 서비스 호출
        orders.requestShipment();
    }

}
