package com.sling.techtest.infrastructure.adapter.out.persistence;

import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(SearchJpaAdapter.class)
public class SearchJpaAdapterTest {

    @Autowired
    private SearchJpaAdapter jpaAdapter;

    @Autowired
    private SpringDataSearchRepository springDataSearchRepository;

    @BeforeEach
    void setUp() {
        springDataSearchRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindById() {
        HotelSearch search = new HotelSearch(
            new HotelSearchId("test-id"),
            "hotel-1",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 1, 5),
            List.of(10, 20)
        );

        jpaAdapter.save(search);

        Optional<HotelSearch> found = jpaAdapter.findById(new HotelSearchId("test-id"));
        assertTrue(found.isPresent());
        assertEquals("hotel-1", found.get().hotelId());
        assertEquals(2, found.get().ages().size());
    }

    @Test
    void shouldCountIdenticalSearches() {
        HotelSearch search1 = new HotelSearch(new HotelSearchId("id1"), "hotel-1", LocalDate.now(), LocalDate.now(), List.of(1));
        HotelSearch search2 = new HotelSearch(new HotelSearchId("id2"), "hotel-1", LocalDate.now(), LocalDate.now(), List.of(1));
        HotelSearch searchDiff = new HotelSearch(new HotelSearchId("id3"), "hotel-1", LocalDate.now(), LocalDate.now(), List.of(2));

        jpaAdapter.save(search1);
        jpaAdapter.save(search2);
        jpaAdapter.save(searchDiff);

        long count = jpaAdapter.countIdenticalSearches(search1);
        assertEquals(2, count);
    }
}
