package com.sling.techtest.application.port.in;

import com.sling.techtest.domain.model.HotelSearchId;

public interface CreateSearchUseCase {
    HotelSearchId createSearch(CreateSearchCommand command);
}
