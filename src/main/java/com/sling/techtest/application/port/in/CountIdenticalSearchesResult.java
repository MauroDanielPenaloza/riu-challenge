package com.sling.techtest.application.port.in;

import com.sling.techtest.domain.model.HotelSearch;

public record CountIdenticalSearchesResult(
    String searchId,
    HotelSearch search,
    long count
) {}
