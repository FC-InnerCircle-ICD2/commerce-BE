package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.infra.advice.exceptions.RequiredValueException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;

public class SelfValidation<T> {

    public void valid(Validator validator) {
        Set<ConstraintViolation<SelfValidation<T>>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new RequiredValueException(violations.toString());
        }
    }

}
