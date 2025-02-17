package com.example.filter.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = NumericValueFilterValidator.class)
public @interface ValidateNumericValueFilter {
    String message() default "Invalid NumericValueFilter";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
