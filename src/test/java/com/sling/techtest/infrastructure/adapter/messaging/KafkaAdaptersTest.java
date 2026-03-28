package com.sling.techtest.infrastructure.adapter.messaging;

import com.sling.techtest.domain.model.HotelSearch;
import com.sling.techtest.domain.model.HotelSearchId;
import com.sling.techtest.domain.port.out.HotelSearchRepositoryPort;
import com.sling.techtest.infrastructure.adapter.in.messaging.KafkaSearchConsumerAdapter;
import com.sling.techtest.infrastructure.adapter.out.messaging.KafkaSearchPublisherAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class KafkaAdaptersTest {

    @Autowired
    private KafkaSearchPublisherAdapter publisherAdapter;

    @MockBean
    private HotelSearchRepositoryPort repositoryPort;

    @Test
    void shouldPublishAndConsumeMessage() throws Exception {
        HotelSearch search = new HotelSearch(
            new HotelSearchId("test-kafka-id"),
            "1234",
            LocalDate.now(),
            LocalDate.now().plusDays(1),
            List.of(30)
        );

        publisherAdapter.publish(search);

        // Verification relies on the consumer calling the repository
        verify(repositoryPort, timeout(5000)).save(any(HotelSearch.class));
    }
}
