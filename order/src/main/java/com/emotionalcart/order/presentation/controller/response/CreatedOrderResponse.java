package com.emotionalcart.order.presentation.controller.response;

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

    /**
     * 주문 금액
     */
    private double totalPrice;

    /**
     * 배송비
     */
    private double deliveryFee;

    /**
     * 주문 상품 목록
     */
    private List<CreatedOrderItemsResponse> orderItems;

    /**
     * 배송 정보
     */
    private DeliveryInfoResponse deliveryInfo;

    @Getter
    private static class CreatedOrderItemsResponse {

        private String productName;

        private double price;

        private int quantity;

    }

    /**
     * <h2>배송 정보 응답</h2>
     * 배송지 정보를 반환한다.
     */
    @Getter
    private static class DeliveryInfoResponse {

        /**
         * 배송지 주소
         */
        private String recipientAddress;

        /**
         * 수령인 이름
         */
        private String recipientName;

        /**
         * 수령인 전화번호
         */
        private String recipientPhone;

        /**
         * 배송 요청사항
         */
        private String deliveryMemo;

    }

}
