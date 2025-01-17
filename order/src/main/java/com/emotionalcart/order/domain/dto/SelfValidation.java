package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.infra.advice.exceptions.RequiredValueException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class SelfValidation<T> {

    private final Validator validator;

    protected SelfValidation() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public void valid() {
        Set<ConstraintViolation<SelfValidation<T>>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new RequiredValueException(violations.toString());
        }
    }

}
