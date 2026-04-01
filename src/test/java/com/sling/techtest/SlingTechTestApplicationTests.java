package com.sling.techtest;

import com.sling.techtest.infrastructure.adapter.out.persistence.model.SearchEntity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import com.sling.techtest.infrastructure.adapter.out.persistence.model.SearchAge;

@SpringBootTest
class SlingTechTestApplicationTests {

    @Test
    void mainShouldStartApplication() {
        assertDoesNotThrow(() -> {
            SlingTechTestApplication.main(new String[] {});
        });
    }

    @Test
    void testSearchEntityGettersAndSetters() {
        SearchEntity entity = new SearchEntity();
        entity.setSearchId("test-search-id");
        entity.setHotelId("test-hotel-id");
        entity.setCheckIn(LocalDate.of(2023, 1, 1));
        entity.setCheckOut(LocalDate.of(2023, 1, 5));

        SearchAge age1 = new SearchAge(entity, 0, 10);
        SearchAge age2 = new SearchAge(entity, 1, 20);

        entity.addSearchAge(age1);
        entity.addSearchAge(age2);

        assertEquals("test-search-id", entity.getSearchId());
        assertEquals("test-hotel-id", entity.getHotelId());
        assertEquals(LocalDate.of(2023, 1, 1), entity.getCheckIn());
        assertEquals(LocalDate.of(2023, 1, 5), entity.getCheckOut());
        assertEquals(2, entity.getSearchAges().size());

        SearchEntity entity2 = new SearchEntity("id2", "h2", LocalDate.now(), LocalDate.now(), new ArrayList<>());
        assertEquals("id2", entity2.getSearchId());
    }

}
