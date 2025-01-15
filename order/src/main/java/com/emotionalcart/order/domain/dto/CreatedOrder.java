package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.domain.entity.OrderItem;
import com.emotionalcart.order.domain.entity.OrderRecipient;
import com.emotionalcart.order.domain.entity.Orders;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 생성된 주문
 */
@Getter
public class CreatedOrder {

    private Long orderId;

    private String paymentMethodName;

    private double totalPrice;

    private LocalDateTime orderAt;

    private List<CreatedOrderItem> orderItems;

    private CreatedDeliveryInfo deliveryInfo;

    public static CreatedOrder from(Orders orders) {
        CreatedOrder createdOrder = new CreatedOrder();
        createdOrder.orderId = orders.getId();
        createdOrder.paymentMethodName = orders.getPaymentMethod().getMethodName();
        createdOrder.totalPrice = orders.getTotalPrice();
        createdOrder.orderAt = orders.getOrderAt();
        createdOrder.orderItems = orders.getOrderItems().stream().map(CreatedOrderItem::from).toList();
        createdOrder.deliveryInfo = CreatedDeliveryInfo.from(orders.getOrderRecipient());
        return createdOrder;
    }

    @Getter
    public static class CreatedOrderItem {

        private Long orderItemId;

        private Long productId;

        private String productName;

        private double orderItemPrice;

        private int quantity;

        public static CreatedOrderItem from(OrderItem orderItem) {
            CreatedOrderItem createdOrderItem = new CreatedOrderItem();
            createdOrderItem.orderItemId = orderItem.getId();
            createdOrderItem.productId = orderItem.getProductId();
            createdOrderItem.productName = orderItem.getProductName();
            createdOrderItem.orderItemPrice = orderItem.getOrderItemPriceDouble();
            createdOrderItem.quantity = orderItem.getQuantity();
            return createdOrderItem;
        }

    }

    @Getter
    public static class CreatedDeliveryInfo {

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

        public static CreatedDeliveryInfo from(OrderRecipient recipient) {
            CreatedDeliveryInfo createDeliveryInfo = new CreatedDeliveryInfo();
            createDeliveryInfo.recipientName = recipient.getRecipientName();
            createDeliveryInfo.recipientPhone = recipient.getRecipientPhone();
            createDeliveryInfo.recipientAddress = recipient.getMappedAddressInfo();

            return createDeliveryInfo;
        }

    }

}
