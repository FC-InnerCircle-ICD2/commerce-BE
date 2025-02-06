package com.emotionalcart.order.infra.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {

    String message() default "유효하지 않은 핸드폰 번호입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
