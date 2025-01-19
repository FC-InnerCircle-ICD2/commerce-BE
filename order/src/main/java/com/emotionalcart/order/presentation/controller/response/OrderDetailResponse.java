package com.emotionalcart.order.presentation.controller.response;

import com.emotionalcart.order.domain.dto.OrderDetail;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailResponse {

    /**
     * 주문 식별자
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    /**
     * 배송 정보
     */
    private DeliveryInfoResponse deliveryInfo;

    public static OrderDetailResponse from(OrderDetail orderDetail) {
        OrderDetailResponse response = new OrderDetailResponse();
        response.orderId = orderDetail.getOrderId();
        response.deliveryInfo = DeliveryInfoResponse.from(orderDetail.getDeliveryInfo());
        return response;
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

        public static DeliveryInfoResponse from(OrderDetail.DeliveryDetailInfo deliveryInfo) {
            DeliveryInfoResponse response = new DeliveryInfoResponse();
            response.recipientName = deliveryInfo.getRecipientName();
            response.recipientPhone = deliveryInfo.getRecipientPhone();
            response.recipientAddress = deliveryInfo.getRecipientAddress();
            return response;
        }

    }

}
