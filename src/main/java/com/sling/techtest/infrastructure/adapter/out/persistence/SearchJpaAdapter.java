package com.sling.techtest.infrastructure.adapter.out.persistence;

import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import com.sling.techtest.domain.port.out.HotelSearchRepositoryPort;
import com.sling.techtest.infrastructure.adapter.out.persistence.model.SearchEntity;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SearchJpaAdapter implements HotelSearchRepositoryPort {

    private final SpringDataSearchRepository springDataSearchRepository;

    public SearchJpaAdapter(SpringDataSearchRepository springDataSearchRepository) {
        this.springDataSearchRepository = springDataSearchRepository;
    }

    @Override
    public void save(HotelSearch hotelSearch) {
        SearchEntity entity = new SearchEntity(
                hotelSearch.searchId().value(),
                hotelSearch.hotelId(),
                hotelSearch.checkIn(),
                hotelSearch.checkOut(),
                hotelSearch.ages());
        springDataSearchRepository.save(entity);
    }

    @Override
    public Optional<HotelSearch> findById(HotelSearchId searchId) {
        return springDataSearchRepository.findById(searchId.value())
                .map(entity -> new HotelSearch(
                        new HotelSearchId(entity.getSearchId()),
                        entity.getHotelId(),
                        entity.getCheckIn(),
                        entity.getCheckOut(),
                        entity.getAges()));
    }

    @Override
    public long countIdenticalSearches(HotelSearch hotelSearch) {
        return springDataSearchRepository.countExactSearches(
                hotelSearch.hotelId(),
                hotelSearch.checkIn(),
                hotelSearch.checkOut(),
                hotelSearch.ages());
    }
}
