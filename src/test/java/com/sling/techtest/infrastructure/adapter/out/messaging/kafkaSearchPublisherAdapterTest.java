package com.sling.techtest.infrastructure.adapter.out.messaging;

import com.sling.techtest.domain.exception.SearchPublishException;
import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class kafkaSearchPublisherAdapterTest {

    private static final String TOPIC = "hotel_availability_searches";

    @Mock
    private KafkaTemplate<String, HotelSearch> kafkaTemplate;

    @InjectMocks
    private KafkaSearchPublisherAdapter kafkaSearchPublisherAdapter;

    @Test
    void shouldPublishHotelSearch() {
        HotelSearchId searchId = new HotelSearchId("test-id");
        HotelSearch hotelSearch = new HotelSearch(
                searchId,
                "hotel-1",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                List.of(30)
        );

        kafkaSearchPublisherAdapter.publish(hotelSearch);

        verify(kafkaTemplate).send(eq(TOPIC), eq("test-id"), eq(hotelSearch));
    }

    @Test
    void shouldThrowSearchPublishExceptionWhenKafkaFails() {
        HotelSearchId searchId = new HotelSearchId("test-id");
        HotelSearch hotelSearch = new HotelSearch(
                searchId,
                "hotel-1",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                List.of(30)
        );
        RuntimeException cause = new RuntimeException("Kafka broker unavailable");

        doThrow(cause).when(kafkaTemplate).send(eq(TOPIC), eq("test-id"), eq(hotelSearch));

        SearchPublishException exception = assertThrows(
                SearchPublishException.class,
                () -> kafkaSearchPublisherAdapter.publish(hotelSearch)
        );

        assertEquals(cause, exception.getCause());
    }
}
