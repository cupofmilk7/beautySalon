package ru.cupofmilk.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.cupofmilk.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event event1;
    private Event event2;
    private LocalDateTime date1;
    private LocalDateTime date2;

    @BeforeEach
    void setUp() {
        date1 = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        date2 = LocalDateTime.of(2023, 1, 2, 12, 0, 0);

        event1 = new Event();
        event1.setDescription("Test Event 1");
        event1.setFirstDate(date1);
        event1.setSecondDate(date1.plusHours(1));

        event2 = new Event();
        event2.setDescription("Test Event 2");
        event2.setFirstDate(date2);
        event2.setSecondDate(date2.plusHours(1));

        eventRepository.saveAll(List.of(event1, event2));
    }

    @Test
    void findByFirstDateAfter_returnsCorrectEvents() {
        List<Event> result = eventRepository.findByFirstDateAfter(date1.minusDays(1));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Event::getDescription)
                .containsExactlyInAnyOrder("Test Event 1", "Test Event 2");
    }

    @Test
    void findByFirstDateBetween_returnsCorrectEvents() {
        List<Event> result = eventRepository.findByFirstDateBetween(date1, date2.plusDays(1));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Event::getDescription)
                .containsExactlyInAnyOrder("Test Event 1", "Test Event 2");
    }

    @Test
    void findActiveEvents_returnsCorrectEvents() {
        LocalDateTime now = LocalDateTime.now();

        // Создаем активное событие
        Event activeEvent = new Event();
        activeEvent.setDescription("Active Event");
        activeEvent.setFirstDate(now.minusDays(1));
        activeEvent.setSecondDate(now.plusDays(1));
        eventRepository.save(activeEvent);

        // Создаем неактивное событие
        Event inactiveEvent = new Event();
        inactiveEvent.setDescription("Inactive Event");
        inactiveEvent.setFirstDate(now.minusDays(2));
        inactiveEvent.setSecondDate(now.minusDays(1));
        eventRepository.save(inactiveEvent);

        List<Event> result = eventRepository.findActiveEvents(now);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getDescription()).isEqualTo("Active Event");
    }

    @Test
    void findByMessageDate_returnsCorrectEvents() {
        LocalDateTime messageDate = LocalDateTime.of(2023, 1, 1, 10, 0, 0);
        event1.setMessageDate(messageDate);
        eventRepository.save(event1);

        List<Event> result = eventRepository.findByMessageDate(messageDate);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(event1.getId());
    }
}