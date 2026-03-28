package com.sling.techtest.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con el identificador asignado a la búsqueda")
public record CreateSearchResponse(
    @Schema(description = "Identificador único de la búsqueda (UUID)", example = "a3f2c1d4-7e8b-4f2a-9c1d-0e3b2a1f4c5d")
    String searchId
) {}
