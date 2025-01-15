package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

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
@ToString
@EqualsAndHashCode(callSuper = false)
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

    @Override public void valid() {
        super.valid();
        validateCardNumber();
        validateExpirationDate();
        validateCvc();
    }

    /**
     * 카드 유효기간 검증
     */
    private void validateExpirationDate() {
        if (expirationDate.length() != 5 || !expirationDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            throw new InvalidValueRequestException("만료일은 MM/YY 형식으로 입력해주세요.");
        }
        LocalDate lastDate = getLastDateFromExpirationDate();
        if (lastDate.isBefore(LocalDate.now())) {
            throw new InvalidValueRequestException("유효기간이 만료된 카드입니다.");
        }
    }

    /**
     * <h2>만료일로부터 마지막 날짜를 반환</h2>
     * 만료일이 지났는지 판단하기 위해서 해당 월까지는 유효하다고 판단하기 위해 마지막 날짜를 반환한다.
     *
     * @return 만료일로부터 마지막 날짜
     */
    private LocalDate getLastDateFromExpirationDate() {
        String[] dates = expirationDate.split("/");
        LocalDate expirationLocalDate = LocalDate.of(2000 + Integer.parseInt(dates[1]), Integer.parseInt(dates[0]), 1);
        return expirationLocalDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * <h2>카드 번호 검증</h2>
     * 카드 번호는 16자리 숫자로 구성되어야 하며, 하이픈(-)을 포함할 수 있다. <br/>
     * 카드 번호는 숫자나 하이픈(-)으로만 입력해야 한다.
     */
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

    /**
     * <h2>CVC 검증</h2>
     * CVC는 3자리 숫자로 구성되어야 한다.
     */
    private void validateCvc() {
        if (isNotNumeric(cvc)) {
            throw new InvalidValueRequestException("CVC는 숫자로만 입력해주세요.");
        }

        if (cvc.length() != 3) {
            throw new InvalidValueRequestException("CVC는 3자리여야 합니다.");
        }
    }

    /**
     * <h2>숫자가 아닌 문자열인지 확인</h2>
     * 숫자가 아닌 문자열인지 확인한다.
     *
     * @param str 확인할 문자열
     * @return 숫자가 아닌 문자열이면 true, 숫자이면 false
     */
    public boolean isNotNumeric(String str) {
        if (str.trim().isEmpty()) {
            return true;
        }
        return !str.matches("-?\\d+(\\.\\d+)?");
    }

}
