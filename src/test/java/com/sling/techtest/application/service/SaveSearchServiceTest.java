package com.sling.techtest.application.service;

import com.sling.techtest.application.port.in.SaveSearchCommand;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SaveSearchServiceTest {

    @Mock
    private HotelSearchRepositoryPort repositoryPort;

    @InjectMocks
    private SaveSearchService saveSearchService;

    @Test
    void shouldSaveSearch() {
        HotelSearchId searchId = new HotelSearchId("test-id");
        SaveSearchCommand command = new SaveSearchCommand(
                searchId,
                "hotel-1",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                List.of(30)
        );

        saveSearchService.saveSearch(command);

        verify(repositoryPort).save(any(HotelSearch.class));
    }
}
