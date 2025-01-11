package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardInfo extends SelfValidation<CardInfo> {

    @NotNull(message = "카드 번호를 입력해주세요.")
    private String cardNumber;
    @NotNull(message = "만료일을 입력해주세요.")
    private String expirationDate;
    @NotNull(message = "CVC를 입력해주세요.")
    private String cvc;
    @NotNull(message = "카드 소유자 이름을 입력해주세요.")
    private String cardOwnerName;

    public static CardInfo createNewCardInfo(String cardNumber, String expirationDate, String cvc, String cardOwnerName) {
        return new CardInfo(cardNumber, expirationDate, cvc, cardOwnerName);
    }

    @Override public void valid(Validator validator) {
        super.valid(validator);
        validateCardNumber();
        validateExpirationDate();
        validateCvc();
    }

    private void validateExpirationDate() {
        if (!expirationDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            throw new InvalidValueRequestException("만료일은 MM/YY 형식으로 입력해주세요.");
        }
    }

    private void validateCardNumber() {
        String validCardNumber = this.cardNumber.replace("-", "");
        if (!StringUtils.hasText(validCardNumber)) {
            throw new InvalidValueRequestException("카드 번호를 올바르게 입력해 주세요.");
        }

        if (isNotNumeric(validCardNumber)) {
            throw new InvalidValueRequestException("카드 번호는 숫자나 하이픈(-)으로만 입력해주세요.");
        }

        if (validCardNumber.length() != 16) {
            throw new InvalidValueRequestException("카드 번호는 16자리여야 합니다.");
        }
    }

    private void validateCvc() {
        if (isNotNumeric(cvc)) {
            throw new InvalidValueRequestException("CVC는 숫자로만 입력해주세요.");
        }

        if (cvc.length() != 3) {
            throw new InvalidValueRequestException("CVC는 3자리여야 합니다.");
        }
    }

    public boolean isNotNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        }
        return !str.matches("-?\\d+(\\.\\d+)?");
    }

}
