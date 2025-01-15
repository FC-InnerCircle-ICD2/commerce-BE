package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.dto.DeliveryInfo;
import com.emotionalcart.order.domain.enums.OrderStatus;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import com.emotionalcart.order.domain.generator.IdGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.util.CollectionUtils;

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
public class Orders extends BaseEntity {

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
     * 결제 식별자
     */
    private String paymentId;

    /**
     * 배송 식별자
     */
    private String shipmentId;

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

    public String getRecipientName() {
        return this.orderRecipient.getRecipientName();
    }

    public String getRecipientPhone() {
        return this.orderRecipient.getRecipientPhone();
    }

    public OrderAddress getRecipientAddress() {
        return this.orderRecipient.getAddress();
    }

}
