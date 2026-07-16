package ru.cupofmilk.mapper;

import org.junit.jupiter.api.Test;
import ru.cupofmilk.dto.event.RequestDto;
import ru.cupofmilk.model.event.Event;
import ru.cupofmilk.model.event.Request;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @Test
    void testToDto_conversion() {
        Request request = new Request();
        request.setEventId(1L);
        request.setNow(true);
        
        Event event = new Event();
        event.setId(1L);
        event.setDescription("Test Description");
        event.setFirstDate(TEST_DATE);
        event.setSecondDate(TEST_DATE.plusHours(1));
        request.setEvent(event);
        
        RequestDto requestDto = RequestMapper.toDto(request);
        
        assertNotNull(requestDto);
        assertEquals(request.getEventId(), requestDto.getEventId());
        assertEquals(request.isNow(), requestDto.isNow());
        
        assertNotNull(requestDto.getEvent());
        assertEquals(event.getId(), requestDto.getEvent().getId());
        assertEquals(event.getDescription(), requestDto.getEvent().getDescription());
        assertEquals(event.getFirstDate(), requestDto.getEvent().getFirstDate());
        assertEquals(event.getSecondDate(), requestDto.getEvent().getSecondDate());
    }

    @Test
    void testToModel_conversion() {
        RequestDto requestDto = new RequestDto();
        requestDto.setEventId(1L);
        requestDto.setNow(true);
        
        ru.cupofmilk.dto.event.EventDto eventDto = new ru.cupofmilk.dto.event.EventDto();
        eventDto.setId(1L);
        eventDto.setDescription("Test Description");
        eventDto.setFirstDate(TEST_DATE);
        eventDto.setSecondDate(TEST_DATE.plusHours(1));
        requestDto.setEvent(eventDto);
        
        Request request = RequestMapper.toModel(requestDto);
        
        assertNotNull(request);
        assertEquals(requestDto.getEventId(), request.getEventId());
        assertEquals(requestDto.isNow(), request.isNow());
        
        assertNotNull(request.getEvent());
        assertEquals(eventDto.getId(), request.getEvent().getId());
        assertEquals(eventDto.getDescription(), request.getEvent().getDescription());
        assertEquals(eventDto.getFirstDate(), request.getEvent().getFirstDate());
        assertEquals(eventDto.getSecondDate(), request.getEvent().getSecondDate());
    }
}