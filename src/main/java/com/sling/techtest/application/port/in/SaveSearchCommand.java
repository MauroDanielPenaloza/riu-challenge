package com.sling.techtest.application.port.in;

import com.sling.techtest.domain.model.HotelSearchId;
import java.time.LocalDate;
import java.util.List;

public record SaveSearchCommand(
    HotelSearchId searchId,
    String hotelId,
    LocalDate checkIn,
    LocalDate checkOut,
    List<Integer> ages
) {}
