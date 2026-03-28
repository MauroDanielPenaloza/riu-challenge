package com.sling.techtest.domain.port.out;

import com.sling.techtest.domain.model.HotelSearch;

public interface HotelSearchPublisherPort {
    void publish(HotelSearch hotelSearch);
}
