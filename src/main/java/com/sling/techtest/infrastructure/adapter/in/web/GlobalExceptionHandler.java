package com.sling.techtest.infrastructure.adapter.in.web;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.sling.techtest.domain.exception.SearchNotFoundException;
import com.sling.techtest.domain.exception.SearchPublishException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("'%s': %s", error.getField(), error.getDefaultMessage()))
                .toList();

        List<String> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        List<String> details = new java.util.ArrayList<>(fieldErrors);
        details.addAll(globalErrors);

        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud inválida",
                "Los datos enviados no son válidos",
                details));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        log.warn("Mensaje HTTP no legible: {}", ex.getMessage());

        List<String> details = List.of();
        if (ex.getCause() instanceof JsonMappingException jsonMappingException) {
            String fieldName = jsonMappingException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));
            if (!fieldName.isBlank()) {
                details = List.of(String.format(
                        "'%s': formato inválido. Verifique que el valor tenga el tipo y formato correctos (fechas: dd/MM/yyyy)",
                        fieldName));
            }
        }

        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud inválida",
                "El cuerpo de la petición no puede ser leído. Verifique el formato de las fechas (dd/MM/yyyy) y los tipos de datos",
                details));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parámetro requerido ausente",
                String.format("El parámetro '%s' es obligatorio", ex.getParameterName()),
                List.of()));
    }

    @ExceptionHandler(SearchNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleSearchNotFound(SearchNotFoundException ex) {
        log.info("Búsqueda no encontrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                List.of()));
    }

    @ExceptionHandler(SearchPublishException.class)
    public ResponseEntity<ApiErrorResponse> handleSearchPublish(SearchPublishException ex) {
        log.error("Error al publicar búsqueda en Kafka: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Servicio no disponible",
                "No fue posible procesar la búsqueda en este momento. Intente nuevamente más tarde",
                List.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                "Ocurrió un error inesperado. Por favor contacte al administrador",
                List.of()));
    }
}
