package com.sling.techtest.infrastructure.adapter.in.messaging;

import com.sling.techtest.application.port.in.SaveSearchCommand;
import com.sling.techtest.application.port.in.SaveSearchUseCase;
import com.sling.techtest.domain.model.HotelSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchConsumerAdapter {

    private static final Logger log = LoggerFactory.getLogger(KafkaSearchConsumerAdapter.class);
    private final SaveSearchUseCase saveSearchUseCase;

    public KafkaSearchConsumerAdapter(SaveSearchUseCase saveSearchUseCase) {
        this.saveSearchUseCase = saveSearchUseCase;
    }

    @KafkaListener(topics = "hotel_availability_searches", groupId = "hotel-search-group")
    public void consume(HotelSearch hotelSearch) {
        log.info("Consumed HotelSearch message for ID: {}", hotelSearch.searchId().value());
        SaveSearchCommand command = new SaveSearchCommand(
                hotelSearch.searchId(),
                hotelSearch.hotelId(),
                hotelSearch.checkIn(),
                hotelSearch.checkOut(),
                hotelSearch.ages()
        );
        saveSearchUseCase.saveSearch(command);
        log.info("Persisted HotelSearch message for ID: {}", hotelSearch.searchId().value());
    }
}
