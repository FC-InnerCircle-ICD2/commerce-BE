package com.emotionalcart.order.presentation.controller.request;

import lombok.Getter;
import lombok.Setter;

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
@Setter
public class CardInfoRequest {

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
