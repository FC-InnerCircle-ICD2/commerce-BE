package com.emotionalcart.order.presentation.controller.response;

import com.emotionalcart.order.domain.dto.CreatedOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h2>주문 생성 응답</h2>
 * 주문 생성 요청에 대한 응답 객체. <br/>
 * 주문 식별자의 경우 Long 타입이므로 클라이언트 중 javascript는 해당 값을 온전히 받지 못해서  {@link ToStringSerializer}를 사용하여 문자열로 변환한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatedOrderResponse {

    /**
     * 주문 식별자
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    private String paymentMethodName;

    /**
     * 주문 금액
     */
    private double totalPrice;

    /**
     * 주문 상품 목록
     */
    private List<CreatedOrderItemsResponse> orderItems;

    /**
     * 배송 정보
     */
    private DeliveryInfoResponse deliveryInfo;

    public static CreatedOrderResponse from(CreatedOrder createdOrder) {
        CreatedOrderResponse response = new CreatedOrderResponse();
        response.orderId = createdOrder.getOrderId();
        response.paymentMethodName = createdOrder.getPaymentMethodName();
        response.totalPrice = createdOrder.getTotalPrice();
        response.orderItems = CreatedOrderItemsResponse.from(createdOrder.getOrderItems());
        response.deliveryInfo = DeliveryInfoResponse.from(createdOrder.getDeliveryInfo());
        return response;
    }

    /**
     * <h2>주문 상품 목록 응답</h2>
     * 주문 상품 목록을 반환한다.
     */
    @Getter
    private static class CreatedOrderItemsResponse {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long orderItemId;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long productId;

        private String productName;

        private double orderItemPrice;

        private int quantity;

        public static List<CreatedOrderItemsResponse> from(List<CreatedOrder.CreatedOrderItem> orderItems) {
            return orderItems.stream().map(CreatedOrderItemsResponse::from).toList();
        }

        private static CreatedOrderItemsResponse from(CreatedOrder.CreatedOrderItem orderItem) {
            CreatedOrderItemsResponse response = new CreatedOrderItemsResponse();
            response.orderItemId = orderItem.getOrderItemId();
            response.productId = orderItem.getProductId();
            response.productName = orderItem.getProductName();
            response.orderItemPrice = orderItem.getOrderItemPrice();
            response.quantity = orderItem.getQuantity();
            return response;
        }

    }

    /**
     * <h2>배송 정보 응답</h2>
     * 배송지 정보를 반환한다.
     */
    @Getter
    private static class DeliveryInfoResponse {

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

        public static DeliveryInfoResponse from(CreatedOrder.CreatedDeliveryInfo deliveryInfo) {
            DeliveryInfoResponse response = new DeliveryInfoResponse();
            response.recipientName = deliveryInfo.getRecipientName();
            response.recipientPhone = deliveryInfo.getRecipientPhone();
            response.recipientAddress = deliveryInfo.getRecipientAddress();
            return response;
        }

    }

}
