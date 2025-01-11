package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.enums.OrderStatus;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import com.emotionalcart.order.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 테이블
 *
 * @author yeji cho
 * @since 2025.1.5
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders {

    @Id
    @IdGenerator
    private Long id;

    /**
     * 결제수단
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    /**
     * 총 주문금액
     */
    @Embedded
    @Column(nullable = false)
    private Money totalPrice;

    /**
     * 주문일시
     */
    @Column(nullable = false)
    private LocalDateTime orderAt;

    /**
     * 주문 회원 식별자
     */
    @CreatedBy
    private Long orderMemberId;

    /**
     * 주문상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    /**
     * 주문 항목 목록
     */
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    /**
     * 수신자 정보
     */
    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderRecipient orderRecipient;

    /**
     * <h2>주문 엔티티 생성</h2>
     * 주문 생성 요청 객체를 받아서 주문 엔티티를 생성한다.<br/>
     * 주문의 총 합계는 주문 항목의 금액과 수량을 곱한 금액의 합이다.{@link Money#sum(List)}<br/>
     *
     * @param createOrder 주문 생성 요청 객체
     * @return 주문 엔티티
     */
    public static Orders createOrder(CreateOrder createOrder) {
        Orders orders = new Orders();
        orders.paymentMethod = createOrder.getPaymentMethod();
        orders.totalPrice =
            Money.sum(createOrder.getOrderItems().stream().map(item -> PriceAndQuantity.of(item.getPrice(), item.getQuantity())).toList());
        orders.createOrderItems(createOrder.getOrderItems());
        return orders;
    }

    public static Orders defaultOrder() {
        return new Orders();
    }

    private void createOrderItems(List<CreateOrderItem> orderItems) {
        this.orderItems = orderItems.stream().map(orderItem -> OrderItem.createOrderItem(this, orderItem)).toList();
    }

    public void finishPayment() {
        this.status = OrderStatus.PAYMENT_COMPLETED;
    }

    public void finishShipment() {
        this.status = OrderStatus.READY_TO_SHIP;
    }

}
