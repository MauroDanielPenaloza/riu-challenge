package com.sling.techtest.application.service;

import com.sling.techtest.application.port.in.SaveSearchCommand;
import com.sling.techtest.application.port.in.SaveSearchUseCase;
import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.port.out.HotelSearchRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class SaveSearchService implements SaveSearchUseCase {

    private final HotelSearchRepositoryPort repositoryPort;

    public SaveSearchService(HotelSearchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public void saveSearch(SaveSearchCommand command) {
        HotelSearch hotelSearch = new HotelSearch(
                command.searchId(),
                command.hotelId(),
                command.checkIn(),
                command.checkOut(),
                command.ages()
        );
        repositoryPort.save(hotelSearch);
    }
}
