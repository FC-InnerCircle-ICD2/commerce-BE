package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.domain.enums.PaymentMethod;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>주문 생성 DTO</h2>
 */
@Getter
@Builder
public class CreateOrder extends SelfValidation<CreateOrder> {

    /**
     * 결제 수단
     */
    @NotNull(message = "결제 수단을 선택해주세요.")
    private PaymentMethod paymentMethod;

    /**
     * 카드 정보
     * 결제 수단이 카드일 경우 필수
     */
    private CardInfo cardInfo;

    /**
     * 배송 정보
     */
    @NotNull(message = "배송 정보를 입력해주세요.")
    private DeliveryInfo deliveryInfo;

    /**
     * 상품 주문 목록
     */
    @NotNull(message = "주문 상품을 선택해주세요.")
    private List<CreateOrderItem> orderItems;

    public static CreateOrder ofPaymentMethod(String payment) {
        return CreateOrder.builder()
            .paymentMethod(PaymentMethod.valueOf(payment))
            .build();
    }

    /**
     * 주문 상품 추가
     *
     * @param productId       상품 식별자
     * @param productOptionId 상품 옵션 식별자
     * @param productName     상품명
     * @param price           상품 금액
     * @param quantity        수량
     */
    public void addItem(Long productId, Long productOptionId, String productName, double price, int quantity) {
        if (CollectionUtils.isEmpty(orderItems))
            orderItems = new ArrayList<>();
        orderItems.add(CreateOrderItem.builder()
                           .productId(productId)
                           .productOptionId(productOptionId)
                           .productName(productName)
                           .price(price)
                           .quantity(quantity)
                           .build());
    }

    @Override
    public void valid() {
        super.valid();
        if (paymentMethod != PaymentMethod.CARD) {
            throw new InvalidValueRequestException("결제 수단은 카드만 가능합니다.");
        }
        if (cardInfo == null) {
            throw new InvalidValueRequestException("카드 정보를 입력해주세요.");
        }
        this.cardInfo.valid();
        this.validItems();
        this.deliveryInfo.valid();
    }

    public void createNewCardInfo(String cardNumber, String expirationDate, String cvc, String cardOwnerName) {
        this.cardInfo = CardInfo.createNewCardInfo(cardNumber, expirationDate, cvc, cardOwnerName);
    }

    public void createDeliveryInfo(String name, String phone, String zoneCode, String address, String detailAddress, String deliveryMemo) {
        this.deliveryInfo = DeliveryInfo.createDeliveryInfo(name, phone, zoneCode, address, detailAddress, deliveryMemo);
    }

    public void validItems() {
        for (CreateOrderItem orderItem : orderItems) {
            orderItem.valid();
        }
    }

}
