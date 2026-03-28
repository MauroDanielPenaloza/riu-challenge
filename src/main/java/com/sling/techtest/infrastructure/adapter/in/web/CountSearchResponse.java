package com.sling.techtest.infrastructure.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Respuesta con los datos de la búsqueda y el número de veces que fue realizada")
public record CountSearchResponse(
    @Schema(description = "Identificador único de la búsqueda", example = "a3f2c1d4-7e8b-4f2a-9c1d-0e3b2a1f4c5d")
    String searchId,

    @Schema(description = "Datos originales de la búsqueda")
    SearchDetails search,

    @Schema(description = "Número de veces que se realizó una búsqueda con exactamente los mismos parámetros", example = "5")
    long count
) {
    @Schema(description = "Detalle de los parámetros de la búsqueda hotelera")
    public record SearchDetails(
        @Schema(description = "Identificador del hotel", example = "1234aBc")
        String hotelId,

        @Schema(description = "Fecha de entrada en formato dd/MM/yyyy", type = "string", example = "29/12/2023")
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkIn,

        @Schema(description = "Fecha de salida en formato dd/MM/yyyy", type = "string", example = "31/12/2023")
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkOut,

        @Schema(description = "Edades de los huéspedes", example = "[30, 29, 1, 3]")
        List<Integer> ages
    ) {}
}
