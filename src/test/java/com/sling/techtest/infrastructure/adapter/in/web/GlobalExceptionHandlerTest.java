package com.sling.techtest.infrastructure.adapter.in.web;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.sling.techtest.domain.exception.SearchNotFoundException;
import com.sling.techtest.domain.exception.SearchPublishException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    private static final String SEARCH_ID = "test-search-id";

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleValidationWithOnlyFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("createSearchRequest", "hotelId",
                "El identificador del hotel es obligatorio y no puede estar vacío");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(bindingResult.getGlobalErrors()).thenReturn(List.of());

        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value()),
                () -> assertEquals("Solicitud inválida", response.getBody().error()),
                () -> assertEquals("Los datos enviados no son válidos", response.getBody().message()),
                () -> assertEquals(1, response.getBody().details().size()),
                () -> assertTrue(response.getBody().details()
                        .contains("'hotelId': El identificador del hotel es obligatorio y no puede estar vacío"))
        );
    }

    @Test
    void shouldHandleValidationWithFieldAndGlobalErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("createSearchRequest", "ages",
                "La lista de edades es obligatoria y no puede estar vacía");
        ObjectError globalError = new ObjectError("createSearchRequest",
                "La fecha de entrada (checkIn) debe ser estrictamente anterior a la fecha de salida (checkOut)");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(bindingResult.getGlobalErrors()).thenReturn(List.of(globalError));

        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value()),
                () -> assertEquals(2, response.getBody().details().size()),
                () -> assertTrue(response.getBody().details()
                        .contains("'ages': La lista de edades es obligatoria y no puede estar vacía")),
                () -> assertTrue(response.getBody().details()
                        .contains("La fecha de entrada (checkIn) debe ser estrictamente anterior a la fecha de salida (checkOut)"))
        );
    }

    @Test
    void shouldHandleUnreadableMessageWithKnownField() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference(null, "checkIn");
        JsonMappingException jsonMappingException = mock(JsonMappingException.class);
        when(jsonMappingException.getPath()).thenReturn(List.of(reference));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("parse error", jsonMappingException, null);

        ResponseEntity<ApiErrorResponse> response = handler.handleUnreadableMessage(ex);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value()),
                () -> assertEquals("Solicitud inválida", response.getBody().error()),
                () -> assertTrue(response.getBody().message().contains("dd/MM/yyyy")),
                () -> assertEquals(1, response.getBody().details().size()),
                () -> assertTrue(response.getBody().details().getFirst().contains("checkIn"))
        );
    }

    @Test
    void shouldHandleUnreadableMessageWithJsonMappingExceptionButBlankFieldName() {
        JsonMappingException.Reference referenceWithNullField = mock(JsonMappingException.Reference.class);
        when(referenceWithNullField.getFieldName()).thenReturn(null);
        JsonMappingException jsonMappingException = mock(JsonMappingException.class);
        when(jsonMappingException.getPath()).thenReturn(List.of(referenceWithNullField));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("parse error", jsonMappingException, null);

        ResponseEntity<ApiErrorResponse> response = handler.handleUnreadableMessage(ex);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value()),
                () -> assertEquals("Solicitud inválida", response.getBody().error()),
                () -> assertTrue(response.getBody().message().contains("dd/MM/yyyy")),
                () -> assertTrue(response.getBody().details().isEmpty())
        );
    }

    @Test
    void shouldHandleUnreadableMessageWithoutFieldInfo() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("parse error",
                new RuntimeException("causa genérica"), null);

        ResponseEntity<ApiErrorResponse> response = handler.handleUnreadableMessage(ex);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value()),
                () -> assertEquals("Solicitud inválida", response.getBody().error()),
                () -> assertTrue(response.getBody().message().contains("dd/MM/yyyy")),
                () -> assertTrue(response.getBody().details().isEmpty())
        );
    }

    @Test
    void shouldHandleMissingRequestParameter() {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("searchId", "String");

        ResponseEntity<ApiErrorResponse> response = handler.handleMissingParam(ex);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value()),
                () -> assertEquals("Parámetro requerido ausente", response.getBody().error()),
                () -> assertTrue(response.getBody().message().contains("searchId")),
                () -> assertTrue(response.getBody().details().isEmpty())
        );
    }

    @Test
    void shouldHandleSearchNotFound() {
        SearchNotFoundException ex = new SearchNotFoundException(SEARCH_ID);

        ResponseEntity<ApiErrorResponse> response = handler.handleSearchNotFound(ex);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value()),
                () -> assertEquals("Recurso no encontrado", response.getBody().error()),
                () -> assertTrue(response.getBody().message().contains(SEARCH_ID)),
                () -> assertTrue(response.getBody().details().isEmpty())
        );
    }

    @Test
    void shouldHandleSearchPublishException() {
        SearchPublishException ex = new SearchPublishException(SEARCH_ID, new RuntimeException("Kafka no disponible"));

        ResponseEntity<ApiErrorResponse> response = handler.handleSearchPublish(ex);

        assertAll(
                () -> assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatusCode().value()),
                () -> assertEquals("Servicio no disponible", response.getBody().error()),
                () -> assertFalse(response.getBody().message().isBlank()),
                () -> assertTrue(response.getBody().details().isEmpty())
        );
    }

    @Test
    void shouldHandleUnexpectedException() {
        Exception ex = new RuntimeException("Error inesperado de prueba");

        ResponseEntity<ApiErrorResponse> response = handler.handleUnexpected(ex);

        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value()),
                () -> assertEquals("Error interno del servidor", response.getBody().error()),
                () -> assertFalse(response.getBody().message().isBlank()),
                () -> assertTrue(response.getBody().details().isEmpty())
        );
    }
}
