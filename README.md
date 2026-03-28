# Hotel Search Service — Challenge técnico

API REST desarrollada con Spring Boot 3, arquitectura hexagonal, Apache Kafka, Oracle DB y virtual threads de Java 21.

---

## Levantar la aplicación

El único requisito es tener **Docker Compose** instalado. La compilación, la base de datos y el broker de Kafka están todos contenedorizados.

```bash
# Iniciar todos los servicios (compila + levanta)
docker compose up --build

# Detener
docker compose down

# Detener y eliminar volúmenes (borra datos de Oracle)
docker compose down -v
```

La primera ejecución puede demorar unos minutos mientras Docker descarga las imágenes de Oracle y Maven resuelve las dependencias.

### Prueba rápida

```bash
# 1. Registrar una búsqueda
curl -s -X POST http://localhost:8080/search \
  -H "Content-Type: application/json" \
  -d '{"hotelId":"1234aBc","checkIn":"29/12/2023","checkOut":"31/12/2023","ages":[30,29,1,3]}'

# 2. Consultar el conteo con el searchId devuelto
curl -s "http://localhost:8080/count?searchId=<searchId>"
```

---

## Swagger / OpenAPI

Con la aplicación en ejecución, la documentación interactiva está disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

Especificación OpenAPI en JSON:

```
http://localhost:8080/v3/api-docs
```

---

## Endpoints

| Método | Path      | Descripción                                              |
|--------|-----------|----------------------------------------------------------|
| POST   | `/search` | Registra una búsqueda y devuelve un `searchId` (UUID)    |
| GET    | `/count`  | Devuelve la búsqueda original y cuántas veces se repitió |

### POST /search

**Body**
```json
{
  "hotelId": "1234aBc",
  "checkIn": "29/12/2023",
  "checkOut": "31/12/2023",
  "ages": [30, 29, 1, 3]
}
```

**Respuesta — 201 Created**
```json
{ "searchId": "a3f2c1d4-7e8b-4f2a-9c1d-0e3b2a1f4c5d" }
```

Devuelve `400 Bad Request` si el payload es inválido (campos requeridos ausentes, check-in posterior al check-out, etc.).

### GET /count?searchId={searchId}

**Respuesta — 200 OK**
```json
{
  "searchId": "a3f2c1d4-7e8b-4f2a-9c1d-0e3b2a1f4c5d",
  "search": {
    "hotelId": "1234aBc",
    "checkIn": "29/12/2023",
    "checkOut": "31/12/2023",
    "ages": [30, 29, 1, 3]
  },
  "count": 5
}
```

Devuelve `404 Not Found` si el `searchId` no existe.

> El orden de las edades es significativo: `[1, 3]` y `[3, 1]` se cuentan como búsquedas distintas.

---

## Implementación

La aplicación sigue **arquitectura hexagonal (Ports & Adapters)** organizada en tres capas de paquetes sin módulos Maven:

- **`domain`** — entidades y puertos como interfaces puras Java, sin dependencias de framework.
- **`application`** — casos de uso (`CreateSearchService`, `CountIdenticalSearchesService`) que orquestan la lógica de negocio.
- **`infrastructure`** — adaptadores IN (REST, Kafka consumer) y OUT (Kafka publisher, JPA/Oracle).

**Decisiones técnicas relevantes:**

- Todas las entidades y DTOs son `record` de Java → inmutabilidad garantizada por el compilador.
- El `searchId` se genera con `UUID.randomUUID()` sin consultar la base de datos.
- El consumidor Kafka persiste los mensajes con `@Async` sobre virtual threads (`spring.threads.virtual.enabled=true`), lo que permite alta concurrencia con mínimo uso de memoria.
- Las edades se almacenan como cadena separada por comas (`"30,29,1,3"`) mediante `IntegerListConverter`, por lo que la consulta de conteo compara el string exacto y respeta el orden.
- Las fechas se manejan con `LocalDate` y `@JsonFormat(pattern = "dd/MM/yyyy")`.

---

## Cobertura de tests (JaCoCo)

El build falla si la cobertura cae por debajo del **80 %** en instrucciones, líneas, ramas y métodos.

```bash
# Ejecutar tests y generar reporte
./mvnw verify
```

El reporte HTML queda en `target/site/jacoco/index.html`.

```bash
# Solo ejecutar tests (sin validación de cobertura)
./mvnw test
```

Los tests incluyen: unitarios con Mockito, integración con `@WebMvcTest`, `@DataJpaTest` y Kafka embebido, y tests de contrato con Pact.

---

## Tests de contrato (Pact)

Se utilizó [Pact JVM](https://docs.pact.io/) para verificar el contrato entre el consumidor y el proveedor del endpoint `POST /search`.

- **`SearchPactConsumerTest`** — define las expectativas del consumidor (qué request envía y qué response espera) y genera el archivo de contrato en `target/pacts/`.
- **`SearchPactProviderTest`** — levanta el contexto de Spring y verifica que el proveedor cumpla exactamente con el contrato generado por el consumidor.

Este enfoque garantiza que los cambios en la API no rompan silenciosamente a los consumidores, detectando incompatibilidades en tiempo de compilación/test sin necesidad de desplegar ambos servicios a la vez.

---

## Stack tecnológico

| Componente       | Tecnología                              | Versión  |
|------------------|-----------------------------------------|----------|
| Lenguaje         | Java                                    | 21 (LTS) |
| Framework        | Spring Boot                             | 3.4.1    |
| Mensajería       | Apache Kafka (Confluent Platform)       | 7.5.0    |
| Base de datos    | Oracle Free                             | latest   |
| ORM              | Spring Data JPA / Hibernate             | —        |
| Documentación    | Springdoc OpenAPI / Swagger UI          | 2.7.0    |
| Cobertura        | JaCoCo                                  | 0.8.12   |
| Testing contrato | Pact JVM                                | 4.6.14   |
| Testing          | JUnit 5, Mockito, Spring Test           | —        |
| Build            | Maven (multi-stage Docker build)        | 3.9.6    |
