package com.sling.techtest.application.port.in;

import com.sling.techtest.domain.model.HotelSearchId;
import java.util.Optional;

public interface CountIdenticalSearchesUseCase {
    Optional<CountIdenticalSearchesResult> countSearches(HotelSearchId searchId);
}
