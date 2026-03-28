package com.sling.techtest.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hotelSearchOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Búsqueda de Disponibilidad Hotelera")
                        .description("""
                                Servicio REST para registrar búsquedas de disponibilidad hotelera y consultar \
                                cuántas veces se ha realizado una búsqueda idéntica.

                                ## Flujo principal
                                1. **POST /search** — registra la búsqueda, la publica en Kafka y devuelve un identificador único.
                                2. **GET /count** — devuelve los datos de la búsqueda original junto con el número de veces que se repitió.

                                > El orden de las edades (`ages`) es significativo: `[1, 3]` y `[3, 1]` se consideran búsquedas distintas.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Sling Tech Test")));
    }
}
