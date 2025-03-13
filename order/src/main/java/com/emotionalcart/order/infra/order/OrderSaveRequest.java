package com.emotionalcart.order.infra.order;

import com.emotionalcart.order.domain.entity.Orders;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderSaveRequest implements Serializable {

    private Long id;
    private double totalPrice;
    private LocalDateTime orderAt;

    private Long orderMemberId;

    private String orderMemberName;

    private String orderStatus;

    private List<OrderItem> items;

    private OrderRecipient recipient;

    public static OrderSaveRequest from(Orders orders) {
        return OrderSaveRequest.builder()
            .id(orders.getId())
            .totalPrice(orders.getTotalPrice())
            .orderAt(orders.getOrderAt())
            .orderMemberId(orders.getOrderMemberId())
            .orderMemberName(orders.getOrderMemberName())
            .orderStatus(orders.getStatus().getStatusName())
            .items(orders.getOrderItems().stream()
                       .map(orderItem -> OrderItem.builder()
                           .id(orderItem.getId())
                           .productId(orderItem.getProductId())
                           .categoryId(orderItem.getCategoryId())
                           .productName(orderItem.getProductName())
                           .categoryName(orderItem.getCategoryName())
                           .price(orderItem.getOrderItemPrice().getAmount())
                           .quantity(orderItem.getQuantity())
                           .itemOptions(orderItem.getOrderItemOptions().stream()
                                            .map(orderItemOption -> OrderItemOption.builder()
                                                .id(orderItemOption.getId())
                                                .optionId(orderItemOption.getProductOptionId())
                                                .optionName(orderItemOption.getProductOptionName())
                                                .optionDetailId(orderItemOption.getProductOptionDetailId())
                                                .optionDetailName(orderItemOption.getProductOptionDetailName())
                                                .build()
                                            ).toList()
                           )
                           .build()
                       ).toList()
            )
            .recipient(OrderRecipient.builder()
                           .id(orders.getOrderRecipient().getId())
                           .recipientName(orders.getOrderRecipient().getRecipientName())
                           .recipientPhone(orders.getOrderRecipient().getRecipientPhone())
                           .postalCode(orders.getOrderRecipient().getAddress().getPostalCode())
                           .defaultAddress(orders.getOrderRecipient().getAddress().getDefaultAddress())
                           .detailAddress(orders.getOrderRecipient().getAddress().getDetailAddress())
                           .orderRequirement(orders.getOrderRecipient().getOrderRequirement())
                           .deliveryRequirement(orders.getOrderRecipient().getDeliveryRequirement())
                           .build()
            )
            .build();
    }

    @Getter
    @Builder
    public static class OrderItem implements Serializable {

        private Long id;

        private Long productId;

        private Long categoryId;

        private String productName;

        private String categoryName;

        private double price;

        private int quantity;

        private List<OrderItemOption> itemOptions;

    }

    @Getter
    @Builder
    public static class OrderItemOption implements Serializable {

        private Long id;
        private Long optionId;
        private String optionName;
        private Long optionDetailId;
        private String optionDetailName;

    }

    @Getter
    @Builder
    public static class OrderRecipient implements Serializable {

        private Long id;
        private String recipientName;
        private String recipientPhone;
        private String postalCode;
        private String defaultAddress;
        private String detailAddress;
        private String orderRequirement;
        private String deliveryRequirement;

    }

}
