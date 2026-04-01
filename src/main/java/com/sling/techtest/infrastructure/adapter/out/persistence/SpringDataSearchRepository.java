package com.sling.techtest.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sling.techtest.infrastructure.adapter.out.persistence.model.SearchEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpringDataSearchRepository extends CrudRepository<SearchEntity, String> {
    @Query("SELECT s FROM SearchEntity s LEFT JOIN FETCH s.searchAges WHERE s.hotelId = :hotelId AND s.checkIn = :checkIn AND s.checkOut = :checkOut")
    List<SearchEntity> findByHotelIdAndDatesWithAges(
            @Param("hotelId") String hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);
}
