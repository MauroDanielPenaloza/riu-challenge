package com.sling.techtest.domain.model;

import java.time.LocalDate;
import java.util.List;

public record HotelSearch(
        HotelSearchId searchId,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages) {
    public HotelSearch {
        ages = List.copyOf(ages);
    }

    public static HotelSearch createNew(String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages) {
        return new HotelSearch(new HotelSearchId(), hotelId, checkIn, checkOut, ages);
    }
}
