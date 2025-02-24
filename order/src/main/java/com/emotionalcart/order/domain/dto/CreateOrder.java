package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.domain.entity.PriceAndQuantity;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>주문 생성 DTO</h2>
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
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

    /**
     * 총 주문금액
     */
    @NotNull(message = "총 주문금액을 입력해주세요.")
    private double totalAmount;

    public static CreateOrder ofPaymentMethod(String payment) {
        return CreateOrder.builder()
            .paymentMethod(PaymentMethod.valueOf(payment))
            .build();
    }

    /**
     * 주문 상품 추가
     * 상품 금액
     */
    public void addItem(CreateOrderItem createOrderItem) {
        if (CollectionUtils.isEmpty(orderItems)) {
            orderItems = new ArrayList<>();
        }
        orderItems.add(createOrderItem);
    }

    /**
     * 카드 정보 생성
     *
     * @param cardNumber     카드 번호
     * @param expirationDate 만료일
     * @param cvc            cvc
     * @param cardOwnerName  카드 소유자 이름
     */
    public void createNewCardInfo(String cardNumber, String expirationDate, String cvc, String cardOwnerName) {
        this.cardInfo = CardInfo.createNewCardInfo(cardNumber, expirationDate, cvc, cardOwnerName);
    }

    public void createDeliveryInfo(String name, String phone, String zoneCode, String address, String detailAddress, String deliveryMemo) {
        this.deliveryInfo = DeliveryInfo.createDeliveryInfo(name, phone, zoneCode, address, detailAddress, deliveryMemo);
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

    public void validItems() {
        for (CreateOrderItem orderItem : orderItems) {
            orderItem.valid();
        }
    }

    /**
     * 주문 상품의 금액과 수량 목록을 반환 한다.
     *
     * @return 주문 상품의 금액과 수량 목록
     */
    public List<PriceAndQuantity> getOrderItemsPriceAndQuantity() {
        return this.orderItems.stream().map(item -> {
            Double reduce = item.getOrderItemOptions().stream().map(CreateOrderItemOption::getAdditionalPrice).reduce(
                0d,
                Double::sum
            );
            return PriceAndQuantity.of(item.getPrice(),
                                       reduce,
                                       item.getQuantity());
        }).toList();
    }

    public CreateOrderItem createOrderItem(@NotNull(message = "상품을 선택해주세요.") Long productId,
                                           @NotNull(message = "수량을 입력해주세요.") int quantity,
                                           @NotNull(message = "상품명을 입력해주세요.") String productName,
                                           @NotNull(message = "상품 금액을 입력해주세요.") double price,
                                           @NotNull(message = "상품 카테고리를 확인해주세요.") Long categoryId) {
        return CreateOrderItem.builder()
            .productId(productId)
            .quantity(quantity)
            .productName(productName)
            .price(price)
            .categoryId(categoryId)
            .build();
    }

}
