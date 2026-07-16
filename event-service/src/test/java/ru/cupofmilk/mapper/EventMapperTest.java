package ru.cupofmilk.mapper;

import org.junit.jupiter.api.Test;
import ru.cupofmilk.model.event.Event;
import ru.cupofmilk.dto.event.EventDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @Test
    void testToDto_conversion() {
        Event event = new Event();
        event.setId(1L);
        event.setDescription("Test Description");
        event.setFirstDate(TEST_DATE);
        event.setSecondDate(TEST_DATE.plusHours(1));
        event.setMessageDate(TEST_DATE.minusDays(1));
        
        EventDto eventDto = EventMapper.toDto(event);
        
        assertNotNull(eventDto);
        assertEquals(event.getId(), eventDto.getId());
        assertEquals(event.getDescription(), eventDto.getDescription());
        assertEquals(event.getFirstDate(), eventDto.getFirstDate());
        assertEquals(event.getSecondDate(), eventDto.getSecondDate());
        assertEquals(event.getMessageDate(), eventDto.getMessageDate());
    }

    @Test
    void testToModel_conversion() {
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setDescription("Test Description");
        eventDto.setFirstDate(TEST_DATE);
        eventDto.setSecondDate(TEST_DATE.plusHours(1));
        eventDto.setMessageDate(TEST_DATE.minusDays(1));
        
        Event event = EventMapper.toModel(eventDto);
        
        assertNotNull(event);
        assertEquals(eventDto.getId(), event.getId());
        assertEquals(eventDto.getDescription(), event.getDescription());
        assertEquals(eventDto.getFirstDate(), event.getFirstDate());
        assertEquals(eventDto.getSecondDate(), event.getSecondDate());
        assertEquals(eventDto.getMessageDate(), event.getMessageDate());
    }
}