package com.sling.techtest.application.service;

import com.sling.techtest.application.port.in.CreateSearchCommand;
import com.sling.techtest.application.port.in.CreateSearchUseCase;
import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import com.sling.techtest.domain.port.out.HotelSearchPublisherPort;
import org.springframework.stereotype.Service;

@Service
public class CreateSearchService implements CreateSearchUseCase {

    private final HotelSearchPublisherPort publisherPort;

    public CreateSearchService(HotelSearchPublisherPort publisherPort) {
        this.publisherPort = publisherPort;
    }

    @Override
    public HotelSearchId createSearch(CreateSearchCommand command) {
        HotelSearch search = HotelSearch.createNew(
            command.hotelId(),
            command.checkIn(),
            command.checkOut(),
            command.ages()
        );
        publisherPort.publish(search);
        return search.searchId();
    }
}
