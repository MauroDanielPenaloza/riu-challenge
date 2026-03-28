package com.sling.techtest.infrastructure.adapter.in.web;

import com.sling.techtest.application.port.in.CountIdenticalSearchesResult;
import com.sling.techtest.application.port.in.CountIdenticalSearchesUseCase;
import com.sling.techtest.application.port.in.CreateSearchCommand;
import com.sling.techtest.application.port.in.CreateSearchUseCase;
import com.sling.techtest.domain.model.HotelSearchId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
@Tag(name = "Search hotels", description = "Endpoints para registrar y consultar búsquedas de hoteles")
public class SearchController {

    private final CreateSearchUseCase createSearchUseCase;
    private final CountIdenticalSearchesUseCase countIdenticalSearchesUseCase;

    public SearchController(CreateSearchUseCase createSearchUseCase,
            CountIdenticalSearchesUseCase countIdenticalSearchesUseCase) {
        this.createSearchUseCase = createSearchUseCase;
        this.countIdenticalSearchesUseCase = countIdenticalSearchesUseCase;
    }

    @Operation(summary = "Registrar una búsqueda de disponibilidad de un hotel", description = """
            Valida y registra una nueva búsqueda de disponibilidad de un hotel. \
            El payload se publica en el topic de Kafka `hotel_availability_searches` \
            y se persiste de forma asíncrona en la base de datos. \
            Devuelve un identificador UUID único generado sin consultar la base de datos.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Búsqueda registrada correctamente", content = @Content(schema = @Schema(implementation = CreateSearchResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos (campos requeridos ausentes, "
                    + "fecha de entrada posterior a la de salida, etc.)", content = @Content)
    })
    @PostMapping("/search")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSearchResponse search(@Valid @RequestBody CreateSearchRequest request) {
        CreateSearchCommand command = new CreateSearchCommand(
                request.hotelId(),
                request.checkIn(),
                request.checkOut(),
                request.ages());
        HotelSearchId searchId = createSearchUseCase.createSearch(command);
        return new CreateSearchResponse(searchId.value());
    }

    @Operation(summary = "Contar búsquedas idénticas", description = """
            Devuelve los datos originales de la búsqueda identificada por `searchId` \
            y el número de veces que se ha realizado una búsqueda con exactamente los mismos parámetros \
            (hotel, fechas y edades en el mismo orden).
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda encontrada", content = @Content(schema = @Schema(implementation = CountSearchResponse.class))),
            @ApiResponse(responseCode = "404", description = "No existe ninguna búsqueda con el identificador indicado", content = @Content)
    })
    @GetMapping("/count")
    public ResponseEntity<CountSearchResponse> countSearches(
            @Parameter(description = "Identificador de la búsqueda devuelto por POST /search", example = "a3f2c1d4-7e8b-4f2a-9c1d-0e3b2a1f4c5d", required = true) @RequestParam String searchId) {
        Optional<CountIdenticalSearchesResult> resultOptional = countIdenticalSearchesUseCase
                .countSearches(new HotelSearchId(searchId));

        return resultOptional.map(result -> {
            CountSearchResponse.SearchDetails searchDetails = new CountSearchResponse.SearchDetails(
                    result.search().hotelId(),
                    result.search().checkIn(),
                    result.search().checkOut(),
                    result.search().ages());
            return ResponseEntity.ok(new CountSearchResponse(result.searchId(), searchDetails, result.count()));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
