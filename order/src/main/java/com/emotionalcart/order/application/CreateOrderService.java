package com.emotionalcart.order.application;

import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.infra.order.OrderRepository;
import com.emotionalcart.order.infra.payment.PaymentInfo;
import com.emotionalcart.order.infra.payment.PaymentResponse;
import com.emotionalcart.order.infra.payment.PaymentService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final Validator validator;
    private final PaymentService paymentService;

    public CreateOrderService(OrderRepository orderRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 주문 생성
     *
     * @param createOrder 주문 생성 정보
     * @return 생성된 주문
     */
    @Transactional
    public CreatedOrder createOrder(CreateOrder createOrder) {
        createOrder.valid(validator);
        Orders orders = Orders.createOrder(createOrder);
        orderRepository.save(orders);
        payment(orders);
        shipment(orders);

        return CreatedOrder.defaultOrder();
    }

    private void payment(Orders orders) {
        PaymentResponse paymentResponse = paymentService.pay(PaymentInfo.create(orders));
        // TODO payment 결과 값에 따른 처리
        orders.finishPayment();
    }

    private void shipment(Orders orders) {
        // TODO 배송 서비스 호출
        orders.finishShipment();
    }

}
