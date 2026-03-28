package com.sling.techtest.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Cuerpo de respuesta cuando ocurre un error en la API")
public record ApiErrorResponse(
        @Schema(description = "Código de estado HTTP", example = "400")
        int status,

        @Schema(description = "Categoría del error", example = "Solicitud inválida")
        String error,

        @Schema(description = "Mensaje descriptivo del error", example = "Los datos enviados no son válidos")
        String message,

        @Schema(description = "Lista de errores de validación por campo (solo en errores 400)", nullable = true)
        List<String> details
) {}
