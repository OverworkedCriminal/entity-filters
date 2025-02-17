package com.example.filter.string;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StringValueFilterValidator implements ConstraintValidator<ValidateStringValueFilter, StringValueFilter> {

    @Override
    public boolean isValid(StringValueFilter value, ConstraintValidatorContext context) {
        return switch (value.getType()) {
            case null -> false;
            case EQUAL, EQUAL_IGNORE_CASE -> value.getV() != null;
            case IS_NOT_NULL, IS_NULL -> value.getV() == null;
        };
    }
    
}
