package com.sling.techtest.infrastructure.adapter.out.persistence.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.sling.techtest.infrastructure.adapter.out.persistence.converter.IntegerListConverter;

@Entity
@Table(name = "hotel_searches")
public class SearchEntity {

    @Id
    @Column(name = "search_id")
    private String searchId;

    @Column(name = "hotel_id", nullable = false)
    private String hotelId;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Convert(converter = IntegerListConverter.class)
    @Column(name = "ages", nullable = false)
    private List<Integer> ages;

    public SearchEntity() {
    }

    public SearchEntity(String searchId, String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages) {
        this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ages = ages;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public void setAges(List<Integer> ages) {
        this.ages = ages;
    }
}
