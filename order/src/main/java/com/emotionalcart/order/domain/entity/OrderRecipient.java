package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.domain.dto.DeliveryInfo;
import com.emotionalcart.order.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 수신자
 *
 * @author yeji cho
 * @since 2025.1.5
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRecipient {

    @Id
    @IdGenerator
    private Long id;

    /**
     * 주문과의 관계 (1:1)
     */
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    /**
     * 수신자명
     */
    @Column(nullable = false, length = 50)
    private String recipientName;

    /**
     * 수신자 전화번호
     */
    @Column(nullable = false, length = 11)
    private String recipientPhone;

    /**
     * 주소 정보 (우편번호, 기본주소, 상세주소)
     */
    @Embedded
    private OrderAddress address;

    /**
     * 주문요청사항
     */
    @Column(length = 50)
    private String orderRequirement;

    /**
     * 배송요청사항
     */
    @Column(length = 50)
    private String deliveryRequirement;

    public static OrderRecipient createOrderRecipient(Orders orders, DeliveryInfo deliveryInfo) {
        OrderRecipient orderRecipient = new OrderRecipient();
        orderRecipient.orders = orders;
        orderRecipient.recipientName = deliveryInfo.getName();
        orderRecipient.recipientPhone = deliveryInfo.getPhoneNumber();
        orderRecipient.address =
            OrderAddress.createNewOrderAddress(deliveryInfo.getZonecode(), deliveryInfo.getAddress(), deliveryInfo.getDetailAddress());
        orderRecipient.deliveryRequirement = deliveryInfo.getDeliveryMemo();
        return orderRecipient;
    }

    public String getMappedAddressInfo() {
        return address.getDefaultAddress().concat(" ").concat(address.getDetailAddress());
    }

}
