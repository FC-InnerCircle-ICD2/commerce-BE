package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.CardInfo;
import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.payment.PaymentInfo;
import com.emotionalcart.order.infra.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

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
        // TODO 상품 재고 조회
        payment(orders, createOrder.getCardInfo());
        shipment(orders);
        orders.addHistory();
        // TODO 상품 재고 차감
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
