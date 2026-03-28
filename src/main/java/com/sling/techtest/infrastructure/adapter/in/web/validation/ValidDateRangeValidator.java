package com.sling.techtest.infrastructure.adapter.in.web.validation;

import com.sling.techtest.infrastructure.adapter.in.web.CreateSearchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, CreateSearchRequest> {

    @Override
    public boolean isValid(CreateSearchRequest request, ConstraintValidatorContext context) {
        if (request.checkIn() == null || request.checkOut() == null) {
            return true;
        }
        return request.checkIn().isBefore(request.checkOut());
    }
}
