package com.sling.techtest.infrastructure.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sling.techtest.application.port.in.CountIdenticalSearchesResult;
import com.sling.techtest.application.port.in.CountIdenticalSearchesUseCase;
import com.sling.techtest.application.port.in.CreateSearchUseCase;
import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateSearchUseCase createSearchUseCase;

    @MockitoBean
    private CountIdenticalSearchesUseCase countIdenticalSearchesUseCase;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateSearch() throws Exception {
        when(createSearchUseCase.createSearch(any())).thenReturn(new HotelSearchId("test-id-123"));

        String payload = """
                {
                  "hotelId": "1234aBc",
                  "checkIn": "29/12/2023",
                  "checkOut": "31/12/2023",
                  "ages": [30, 29, 1, 3]
                }
                """;

        mockMvc.perform(post("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.searchId").value("test-id-123"));
    }

    @Test
    void shouldReturnCount() throws Exception {
        HotelSearch search = new HotelSearch(
                new HotelSearchId("xxxxx"), "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(3, 29, 30, 1));
        CountIdenticalSearchesResult result = new CountIdenticalSearchesResult("xxxxx", search, 100);

        when(countIdenticalSearchesUseCase.countSearches(new HotelSearchId("xxxxx"))).thenReturn(Optional.of(result));

        mockMvc.perform(get("/count")
                .param("searchId", "xxxxx"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value("xxxxx"))
                .andExpect(jsonPath("$.count").value(100))
                .andExpect(jsonPath("$.search.hotelId").value("1234aBc"));
    }
}
