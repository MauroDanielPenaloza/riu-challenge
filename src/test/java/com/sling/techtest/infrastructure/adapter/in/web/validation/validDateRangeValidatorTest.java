package com.sling.techtest.infrastructure.adapter.in.web.validation;

import com.sling.techtest.infrastructure.adapter.in.web.CreateSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class validDateRangeValidatorTest {

    private ValidDateRangeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidDateRangeValidator();
    }

    @Test
    void shouldReturnTrueWhenCheckInIsNull() {
        CreateSearchRequest request = new CreateSearchRequest("hotel-1", null, LocalDate.now().plusDays(2), List.of(30));

        boolean result = validator.isValid(request, null);

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenCheckOutIsNull() {
        CreateSearchRequest request = new CreateSearchRequest("hotel-1", LocalDate.now(), null, List.of(30));

        boolean result = validator.isValid(request, null);

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenCheckInIsBeforeCheckOut() {
        CreateSearchRequest request = new CreateSearchRequest(
                "hotel-1",
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 5),
                List.of(30)
        );

        boolean result = validator.isValid(request, null);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenCheckInIsAfterCheckOut() {
        CreateSearchRequest request = new CreateSearchRequest(
                "hotel-1",
                LocalDate.of(2025, 6, 5),
                LocalDate.of(2025, 6, 1),
                List.of(30)
        );

        boolean result = validator.isValid(request, null);

        assertFalse(result);
    }
}
