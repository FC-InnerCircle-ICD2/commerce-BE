package com.emotionalcart.order.presentation.validator;

import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import com.emotionalcart.order.presentation.controller.request.CreateOrderRequest;
import com.emotionalcart.order.presentation.controller.request.PaymentMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

import static com.emotionalcart.order.infra.utils.DateUtil.getLastDateFromExpirationDate;
import static com.emotionalcart.order.infra.utils.NumberUtil.isNotNumeric;

@Component
public class CreateOrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CreateOrderRequest.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CreateOrderRequest request = (CreateOrderRequest)target;
        if (request.getPaymentMethod() != PaymentMethod.CARD) {
            errors.rejectValue("paymentMethod", "error.valid.paymentMethod", "결제 수단은 카드만 선택 가능합니다.");
        }
        validateCardNumber(errors, request);

        validateExpirationDate(request);

        validateCvc(errors, request);

    }

    private static void validateCardNumber(Errors errors, CreateOrderRequest request) {
        String cardNumber = request.getCardNumber().replace("-", "").trim();
        if (isNotNumeric(cardNumber)) {
            errors.rejectValue("cardInfo.cardNumber", "error.valid.cardNumber.number", "카드 번호는 숫자로만 입력해주세요.");
        }
        if (cardNumber.length() != 16) {
            errors.rejectValue("cardInfo.cardNumber", "error.valid.cardNumber.length", "카드 번호는 16자리여야 합니다.");
        }
    }

    private static void validateExpirationDate(CreateOrderRequest request) {
        String expirationDate = request.getExpirationDate();
        if (expirationDate.length() != 5 || !expirationDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            throw new InvalidValueRequestException("만료일은 MM/YY 형식으로 입력해주세요.");
        }
        LocalDate lastDate = getLastDateFromExpirationDate(expirationDate);
        if (lastDate.isBefore(LocalDate.now())) {
            throw new InvalidValueRequestException("유효기간이 만료된 카드입니다.");
        }
    }

    private static void validateCvc(Errors errors, CreateOrderRequest request) {
        String cvc = request.getCvc();
        if (isNotNumeric(cvc)) {
            errors.rejectValue("cardInfo.cvc", "error.valid.cvc.number", "CVC는 숫자로만 입력해주세요.");
        }
        if (cvc.length() != 3) {
            errors.rejectValue("cardInfo.cvc", "error.valid.cvc.length", "CVC는 3자리여야 합니다.");
        }
    }

}
