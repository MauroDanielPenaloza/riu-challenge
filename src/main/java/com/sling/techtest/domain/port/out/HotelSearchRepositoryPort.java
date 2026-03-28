package com.sling.techtest.domain.port.out;

import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import java.util.Optional;

public interface HotelSearchRepositoryPort {
    void save(HotelSearch hotelSearch);
    Optional<HotelSearch> findById(HotelSearchId searchId);
    long countIdenticalSearches(HotelSearch hotelSearch);
}
