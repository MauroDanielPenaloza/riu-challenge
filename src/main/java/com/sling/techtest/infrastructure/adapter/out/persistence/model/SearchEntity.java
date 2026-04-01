package com.sling.techtest.infrastructure.adapter.out.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotel_searches")
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    public void addSearchAge(SearchAge age) {
        searchAges.add(age);
        age.setSearch(this);
    }
}
