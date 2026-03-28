package com.sling.techtest.domain.model;

import java.util.UUID;

public record HotelSearchId(String value) {
    public HotelSearchId() {
        this(UUID.randomUUID().toString());
    }
}
