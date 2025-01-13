package com.emotionalcart.order.presentation.controller.request;

import com.emotionalcart.order.domain.dto.CreateOrder;
import com.emotionalcart.order.infra.validator.ValidPhoneNumber;
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

    /**
     * 배송 정보
     */
    @NotNull(message = "배송 정보를 입력해주세요.")
    private DeliveryRequest delivery;

    /**
     * 주문 생성 요청 객체를 주문 도메인 객체로 변환한다.
     *
     * @return 주문 도메인 객체
     */
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
        createOrder.createDeliveryInfo(delivery.getName(),
                                       delivery.getPhoneNumber(),
                                       delivery.getZonecode(),
                                       delivery.getAddress(),
                                       delivery.getDetailAddress(),
                                       delivery.getDeliveryMemo());
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

    /**
     * <h2>카드 정보 요청</h2>
     * 카드 결제 시 카드 정보를 받는 요청 객체.
     */
    @Getter
    private static class DeliveryRequest {

        /**
         * 수령인 이름
         */
        @NotNull(message = "수령인 이름을 입력해주세요.")
        private String name;
        /**
         * 수령인 전화번호
         */
        @NotNull(message = "수령인 전화번호를 입력해주세요.")
        @ValidPhoneNumber
        private String phoneNumber;

        /**
         * 우편번호
         */
        @NotNull(message = "우편번호를 입력해주세요.")
        private String zonecode;
        /**
         * 배송지 주소
         */
        @NotNull(message = "주소를 입력해주세요.")
        private String address;

        /**
         * 상세주소
         */
        @NotNull(message = "상세주소를 입력해주세요.")
        private String detailAddress;

        /**
         * 배송 메모
         */
        @NotNull(message = "배송 메모를 입력해주세요.")
        private String deliveryMemo;

    }

    /**
     * <h2>카드 정보</h2>
     * <p>주문 시 사용할 카드 정보를 나타내는 클래스.</p>
     * <p>카드 번호, 만료일, CVC, 카드 소유자 이름을 저장한다.</p>
     * <p>카드 번호는 16자리 숫자로 구성되어야 하며, 만료일은 MM/YY 형식이어야 한다.</p>
     * <p>CVC는 3자리 숫자로 구성되어야 한다.</p>
     * <p>카드 소유자 이름은 2자 이상 20자 이하의 문자열이어야 한다.</p>
     * <p>카드 정보는 결제 수단이 카드일 때만 필요하다.{@link com.emotionalcart.order.domain.enums.PaymentMethod#CARD}</p>
     */
    @Getter
    private static class CardInfoRequest {

        /**
         * 카드 번호
         */
        private String cardNumber;
        /**
         * 만료일
         */
        private String expirationDate;
        /**
         * CVC
         */
        private String cvc;
        /**
         * 카드 소유자 이름
         */
        private String cardOwnerName;

    }

}
