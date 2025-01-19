package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.domain.entity.OrderRecipient;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import lombok.Getter;

/**
 * 조회된 주문
 */
@Getter
public class OrderDetail {

    /**
     * 주문번호
     */
    private final Long orderId;

    /**
     * 결제수단
     */
    private final PaymentMethod paymentMethod;

    /**
     * 조회된 배송정보
     */
    private final DeliveryDetailInfo deliveryInfo;

    public OrderDetail(Orders orders) {
        this.orderId = orders.getId();
        this.paymentMethod = orders.getPaymentMethod();
        this.deliveryInfo = DeliveryDetailInfo.from(orders.getOrderRecipient());
    }

    public static OrderDetail from(Orders orders) {
        return new OrderDetail(orders);
    }

    @Getter
    public static class DeliveryDetailInfo {

        /**
         * 수령인 이름
         */
        private String recipientName;
        /**
         * 수령인 전화번호
         */
        private String recipientPhone;
        /**
         * 배송지 주소
         */
        private String recipientAddress;

        public static DeliveryDetailInfo from(OrderRecipient recipient) {
            DeliveryDetailInfo deliveryDetailInfo = new DeliveryDetailInfo();
            deliveryDetailInfo.recipientName = recipient.getRecipientName();
            deliveryDetailInfo.recipientPhone = recipient.getRecipientPhone();
            deliveryDetailInfo.recipientAddress = recipient.getMappedAddressInfo();
            return deliveryDetailInfo;
        }

    }

}
