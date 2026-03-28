package com.sling.techtest.application.service;

import com.sling.techtest.application.port.in.CreateSearchCommand;
import com.sling.techtest.domain.model.HotelSearchId;
import com.sling.techtest.domain.port.out.HotelSearchPublisherPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateSearchServiceTest {

    @Mock
    private HotelSearchPublisherPort publisherPort;

    @InjectMocks
    private CreateSearchService createSearchService;

    @Test
    void shouldCreateSearchAndPublish() {
        CreateSearchCommand command = new CreateSearchCommand(
            "1234aBc",
            LocalDate.of(2023, 12, 29),
            LocalDate.of(2023, 12, 31),
            List.of(30, 29, 1, 3)
        );

        HotelSearchId result = createSearchService.createSearch(command);

        assertNotNull(result);
        assertNotNull(result.value());
        verify(publisherPort).publish(any());
    }
}
