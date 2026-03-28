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

    @Query("SELECT count(s) FROM SearchEntity s WHERE s.hotelId = :hotelId AND s.checkIn = :checkIn AND s.checkOut = :checkOut AND s.ages = :ages")
    long countExactSearches(@Param("hotelId") String hotelId, @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut, @Param("ages") List<Integer> ages);

}
