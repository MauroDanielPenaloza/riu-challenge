package com.sling.techtest.application.service;

import com.sling.techtest.application.port.in.CountIdenticalSearchesResult;
import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import com.sling.techtest.domain.port.out.HotelSearchRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountIdenticalSearchesServiceTest {

    @Mock
    private HotelSearchRepositoryPort repositoryPort;

    @InjectMocks
    private CountIdenticalSearchesService countService;

    @Test
    void shouldReturnEmptyWhenSearchNotFound() {
        HotelSearchId id = new HotelSearchId("non-existent");
        when(repositoryPort.findById(id)).thenReturn(Optional.empty());

        Optional<CountIdenticalSearchesResult> result = countService.countSearches(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnCountWhenSearchFound() {
        HotelSearchId id = new HotelSearchId("xxxxx");
        HotelSearch search = new HotelSearch(id, "1234", LocalDate.now(), LocalDate.now().plusDays(2), List.of(30, 29));

        when(repositoryPort.findById(id)).thenReturn(Optional.of(search));
        when(repositoryPort.countIdenticalSearches(search)).thenReturn(5L);

        Optional<CountIdenticalSearchesResult> result = countService.countSearches(id);

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().count());
        assertEquals("xxxxx", result.get().searchId());
        assertEquals("1234", result.get().search().hotelId());
    }
}
