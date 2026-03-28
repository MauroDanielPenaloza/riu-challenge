package com.sling.techtest.application.service;

import com.sling.techtest.application.port.in.CountIdenticalSearchesResult;
import com.sling.techtest.application.port.in.CountIdenticalSearchesUseCase;
import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import com.sling.techtest.domain.port.out.HotelSearchRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountIdenticalSearchesService implements CountIdenticalSearchesUseCase {

    private final HotelSearchRepositoryPort repositoryPort;

    public CountIdenticalSearchesService(HotelSearchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Optional<CountIdenticalSearchesResult> countSearches(HotelSearchId searchId) {
        Optional<HotelSearch> optionalSearch = repositoryPort.findById(searchId);
        
        if (optionalSearch.isEmpty()) {
            return Optional.empty();
        }
        
        HotelSearch search = optionalSearch.get();
        long count = repositoryPort.countIdenticalSearches(search);
        
        return Optional.of(new CountIdenticalSearchesResult(searchId.value(), search, count));
    }
}
