package com.sling.techtest.infrastructure.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sling.techtest.infrastructure.adapter.in.web.validation.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ValidDateRange
@Schema(description = "Datos de la búsqueda de disponibilidad hotelera")
public record CreateSearchRequest(
    @Schema(description = "Identificador del hotel", example = "1234aBc")
    @NotBlank(message = "El identificador del hotel (hotelId) es obligatorio y no puede estar vacío")
    String hotelId,

    @Schema(description = "Fecha de entrada en formato dd/MM/yyyy", type = "string", example = "29/12/2023")
    @NotNull(message = "La fecha de entrada (checkIn) es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkIn,

    @Schema(description = "Fecha de salida en formato dd/MM/yyyy (debe ser posterior al check-in)", type = "string", example = "31/12/2023")
    @NotNull(message = "La fecha de salida (checkOut) es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkOut,

    @Schema(description = "Edades de los huéspedes. El orden es significativo para el conteo de búsquedas idénticas",
            example = "[30, 29, 1, 3]")
    @NotEmpty(message = "La lista de edades (ages) es obligatoria y no puede estar vacía")
    List<Integer> ages
) {}
