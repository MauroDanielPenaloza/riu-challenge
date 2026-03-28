package com.sling.techtest.infrastructure.adapter.in.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidDateRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {

    String message() default "La fecha de entrada (checkIn) debe ser estrictamente anterior a la fecha de salida (checkOut)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
