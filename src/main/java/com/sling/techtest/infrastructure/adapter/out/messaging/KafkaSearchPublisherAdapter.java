package com.sling.techtest.infrastructure.adapter.out.messaging;

import com.sling.techtest.domain.exception.SearchPublishException;
import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.port.out.HotelSearchPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchPublisherAdapter implements HotelSearchPublisherPort {

    private final KafkaTemplate<String, HotelSearch> kafkaTemplate;
    private static final String TOPIC = "hotel_availability_searches";

    public KafkaSearchPublisherAdapter(KafkaTemplate<String, HotelSearch> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(HotelSearch hotelSearch) {
        try {
            kafkaTemplate.send(TOPIC, hotelSearch.searchId().value(), hotelSearch);
        } catch (Exception ex) {
            throw new SearchPublishException(hotelSearch.searchId().value(), ex);
        }
    }
}
