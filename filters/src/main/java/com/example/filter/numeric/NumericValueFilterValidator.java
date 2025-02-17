package com.example.filter.numeric;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumericValueFilterValidator implements ConstraintValidator<ValidateNumericValueFilter, NumericValueFilter<?>> {

    @Override
    public boolean isValid(NumericValueFilter<?> value, ConstraintValidatorContext context) {
        return switch (value.getType()) {
            case null ->
                false;
            case LESS, LESS_EQUAL, EQUAL, GREATER, GREATER_EQUAL ->
                value.getV1() != null && value.getV2() == null;
            case BETWEEN ->
                value.getV1() != null && value.getV2() != null;
            case IS_NULL, IS_NOT_NULL ->
                value.getV1() == null && value.getV2() == null;
        };
    }
    
}
