package com.sling.techtest;

import com.sling.techtest.infrastructure.adapter.out.persistence.converter.IntegerListConverter;
import com.sling.techtest.infrastructure.adapter.out.persistence.model.SearchEntity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        entity.setAges(List.of(10, 20));

        assertEquals("test-search-id", entity.getSearchId());
        assertEquals("test-hotel-id", entity.getHotelId());
        assertEquals(LocalDate.of(2023, 1, 1), entity.getCheckIn());
        assertEquals(LocalDate.of(2023, 1, 5), entity.getCheckOut());
        assertEquals(2, entity.getAges().size());

        SearchEntity entity2 = new SearchEntity("id2", "h2", LocalDate.now(), LocalDate.now(), List.of(1));
        assertEquals("id2", entity2.getSearchId());
    }

    @Test
    void testIntegerListConverter() {
        IntegerListConverter converter = new IntegerListConverter();

        // Database Column conversions
        assertEquals("", converter.convertToDatabaseColumn(null));
        assertEquals("", converter.convertToDatabaseColumn(List.of()));
        assertEquals("1,2,3", converter.convertToDatabaseColumn(List.of(1, 2, 3)));

        // Entity Attribute conversions
        assertTrue(converter.convertToEntityAttribute(null).isEmpty());
        assertTrue(converter.convertToEntityAttribute("").isEmpty());
        assertTrue(converter.convertToEntityAttribute("   ").isEmpty());

        List<Integer> parsed = converter.convertToEntityAttribute("10,20,30");
        assertEquals(3, parsed.size());
        assertEquals(10, parsed.get(0));
        assertEquals(20, parsed.get(1));
        assertEquals(30, parsed.get(2));
    }
}
