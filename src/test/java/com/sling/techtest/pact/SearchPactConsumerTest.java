package com.sling.techtest.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "SearchProvider")
public class SearchPactConsumerTest {

    @Pact(consumer = "SearchConsumer")
    public V4Pact createPact(PactBuilder builder) {
        return builder
            .usingLegacyDsl()
            .given("API is available")
            .uponReceiving("A request to search hotels")
            .path("/search")
            .method("POST")
            .headers("Content-Type", "application/json")
            .body("{\"hotelId\":\"1234aBc\",\"checkIn\":\"29/12/2023\",\"checkOut\":\"31/12/2023\",\"ages\":[30, 29, 1, 3]}")
            .willRespondWith()
            .status(201)
            .body("{\"searchId\":\"mock-id\"}")
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void testCreateSearch(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String body = "{\"hotelId\":\"1234aBc\",\"checkIn\":\"29/12/2023\",\"checkOut\":\"31/12/2023\",\"ages\":[30, 29, 1, 3]}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        
        String response = restTemplate.postForObject(mockServer.getUrl() + "/search", request, String.class);
        
        assertNotNull(response);
        assertTrue(response.contains("mock-id"));
    }
    
    // Help method as true
    private void assertTrue(boolean condition) {
        assertEquals(true, condition);
    }
}
