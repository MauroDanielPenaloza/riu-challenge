package com.sling.techtest.infrastructure.adapter.in.messaging;

import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.port.out.HotelSearchRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchConsumerAdapter {

    private static final Logger log = LoggerFactory.getLogger(KafkaSearchConsumerAdapter.class);
    private final HotelSearchRepositoryPort repositoryPort;

    public KafkaSearchConsumerAdapter(HotelSearchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @KafkaListener(topics = "hotel_availability_searches", groupId = "hotel-search-group")
    // Virtual Threads is enabled by properties. 
    @Async
    public void consume(HotelSearch hotelSearch) {
        log.info("Consumed HotelSearch message for ID: {}", hotelSearch.searchId().value());
        repositoryPort.save(hotelSearch);
        log.info("Persisted HotelSearch message for ID: {}", hotelSearch.searchId().value());
    }
}
