package com.emotionalcart.order.infra.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // 정규 표현식: 한국 핸드폰 번호 형식
    private static final String PHONE_NUMBER_REGEX = "^010-?\\d{4}-?\\d{4}$";

    private static final Pattern PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // 초기화 작업 필요 시 작성 (여기서는 필요 없음)
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; // null 또는 빈 문자열은 유효하지 않음
        }
        return PATTERN.matcher(value).matches();
    }

}