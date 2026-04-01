package com.sling.techtest.infrastructure.adapter.out.persistence;

import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import com.sling.techtest.domain.port.out.HotelSearchRepositoryPort;
import com.sling.techtest.infrastructure.adapter.out.persistence.model.SearchAge;
import com.sling.techtest.infrastructure.adapter.out.persistence.model.SearchEntity;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
                new ArrayList<>()
        );

        List<Integer> domainAges = hotelSearch.ages();
        for (int i = 0; i < domainAges.size(); i++) {
            SearchAge searchAge = new SearchAge(entity, i, domainAges.get(i));
            entity.addSearchAge(searchAge);
        }

        springDataSearchRepository.save(entity);
    }

    @Override
    public Optional<HotelSearch> findById(HotelSearchId searchId) {
        return springDataSearchRepository.findById(searchId.value())
                .map(this::mapToDomain);
    }

    @Override
    public long countIdenticalSearches(HotelSearch hotelSearch) {
        var matches = springDataSearchRepository.findByHotelIdAndDatesWithAges(
                hotelSearch.hotelId(),
                hotelSearch.checkIn(),
                hotelSearch.checkOut());

        return matches.stream()
                .filter(entity -> {
                    List<Integer> extractedAges = extractAges(entity);
                    return extractedAges.equals(hotelSearch.ages());
                })
                .count();
    }

    private HotelSearch mapToDomain(SearchEntity entity) {
        return new HotelSearch(
                new HotelSearchId(entity.getSearchId()),
                entity.getHotelId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                extractAges(entity)
        );
    }

    private List<Integer> extractAges(SearchEntity entity) {
        if (entity.getSearchAges() == null) {
            return List.of();
        }
        return entity.getSearchAges().stream()
                .sorted(Comparator.comparingInt(SearchAge::getAgeOrder))
                .map(SearchAge::getAge)
                .toList();
    }
}
