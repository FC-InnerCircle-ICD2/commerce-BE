package com.emotionalcart.order.presentation.controller.request;

import com.emotionalcart.order.domain.dto.CreateOrder;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h2>주문 생성 요청</h2>
 * 주문 할 상품 목록을 받아서 주문을 생성하는 요청 객체.<br/>
 * 상품 목록은 `orderItems` 필드로 받는다. {@link CreateOrderItemRequest} 객체로 상품 정보를 받는다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateOrderRequest {

    /**
     * 결제 수단
     */
    @NotNull(message = "결제 수단을 선택해주세요.")
    private PaymentMethod paymentMethod;

    /**
     * 카드 정보
     * 결제 수단이 카드일 경우 필수
     */
    private CardInfoRequest cardInfo;

    /**
     * 상품 주문 목록
     */
    @NotNull(message = "주문 상품을 선택해주세요.")
    private List<CreateOrderItemRequest> orderItems;

    public CreateOrder mapToDomain() {
        CreateOrder createOrder = CreateOrder.ofPaymentMethod(paymentMethod.name());
        if (paymentMethod == PaymentMethod.CARD) {
            createOrder.createNewCardInfo(cardInfo.getCardNumber(),
                                          cardInfo.getExpirationDate(),
                                          cardInfo.getCvc(),
                                          cardInfo.getCardOwnerName());
        }
        for (CreateOrderItemRequest orderItem : orderItems) {
            createOrder.addItem(orderItem.productId, orderItem.productOptionId, orderItem.productName, orderItem.price, orderItem.quantity);
        }
        return createOrder;
    }

    /**
     * <h2>주문 상품 생성 요청</h2>
     * 주문에 포함될 상품을 생성하는 요청 객체. <br/>
     * 상품 식별자, 상품 옵션 식별자, 상품명, 상품 금액, 수량을 받는다. <br/>
     * 주문 생성 요청 객체 {@link CreateOrderRequest}에 포함된다.
     * 상품 옵션 식별자로 재고를 파악한다.
     */
    @Getter
    private static class CreateOrderItemRequest {

        /**
         * 상품 식별자
         */
        @NotNull(message = "상품을 선택해주세요.")
        private Long productId;

        /**
         * 상품 옵션 식별자
         */
        @NotNull(message = "상품 옵션을 선택해주세요.")
        private Long productOptionId;

        /**
         * 상품명
         */
        @NotNull(message = "상품명을 입력해주세요.")
        private String productName;

        /**
         * 상품 금액
         */
        @NotNull(message = "상품 금액을 입력해주세요.")
        private double price;

        /**
         * 수량
         */
        @NotNull(message = "수량을 입력해주세요.")
        private int quantity;

    }

}
