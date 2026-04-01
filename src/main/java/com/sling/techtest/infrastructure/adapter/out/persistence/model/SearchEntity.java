package com.sling.techtest.infrastructure.adapter.out.persistence.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "search", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SearchAge> searchAges = new ArrayList<>();

    public SearchEntity() {
    }

    public SearchEntity(String searchId, String hotelId, LocalDate checkIn, LocalDate checkOut) {
        this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public void addSearchAge(SearchAge age) {
        searchAges.add(age);
        age.setSearch(this);
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

    public java.util.List<SearchAge> getSearchAges() {
        return searchAges;
    }

    public void setSearchAges(java.util.List<SearchAge> searchAges) {
        this.searchAges = searchAges;
    }
}
