package ru.cupofmilk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cupofmilk.model.event.Event;
import ru.cupofmilk.repository.EventRepository;
import ru.cupofmilk.dto.event.EventDto;
import ru.cupofmilk.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private EventDto eventDto;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

        event = new Event();
        event.setId(1L);
        event.setDescription("Test Event");
        event.setFirstDate(testDate);
        event.setSecondDate(testDate.plusHours(1));

        eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setDescription("Test Event");
        eventDto.setFirstDate(testDate);
        eventDto.setSecondDate(testDate.plusHours(1));
    }

    @Test
    void createEvent_success() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDto result = eventService.createEvent(eventDto);

        assertNotNull(result);
        assertEquals(eventDto.getId(), result.getId());
        assertEquals(eventDto.getDescription(), result.getDescription());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void getEvent_success() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        EventDto result = eventService.getEvent(1L);

        assertNotNull(result);
        assertEquals(eventDto.getId(), result.getId());
        assertEquals(eventDto.getDescription(), result.getDescription());
    }

    @Test
    void getEvent_notFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.getEvent(1L));
    }

    @Test
    void deleteEvent_success() {
        when(eventRepository.existsById(1L)).thenReturn(true);

        boolean result = eventService.deleteEvent(1L);

        assertTrue(result);
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEvent_notFound() {
        // Исправлено: existsById возвращает false, метод должен выбросить NotFoundException
        when(eventRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> eventService.deleteEvent(1L));
        verify(eventRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateEvent_success() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);

        EventDto updatedDto = new EventDto();
        updatedDto.setDescription("Updated Description");
        updatedDto.setFirstDate(testDate.plusDays(1));

        EventDto result = eventService.updateEvent(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        assertEquals(testDate.plusDays(1), result.getFirstDate());
    }

    @Test
    void updateEvent_notFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        EventDto updatedDto = new EventDto();
        updatedDto.setDescription("Updated Description");

        assertThrows(NotFoundException.class, () -> eventService.updateEvent(1L, updatedDto));
    }

    @Test
    void getEvents_withDate_success() {
        when(eventRepository.findByFirstDateAfter(testDate)).thenReturn(List.of(event));

        List<EventDto> result = eventService.getEvents(testDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDto.getId(), result.getFirst().getId());
    }

    @Test
    void getEvents_withoutDate_success() {
        LocalDateTime defaultDate = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        when(eventRepository.findByFirstDateAfter(defaultDate)).thenReturn(List.of(event));

        List<EventDto> result = eventService.getEvents(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDto.getId(), result.getFirst().getId());
    }

    @Test
    void getActiveEvents_success() {
        when(eventRepository.findActiveEvents(any(LocalDateTime.class))).thenReturn(List.of(event));

        List<EventDto> result = eventService.getActiveEvents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDto.getId(), result.getFirst().getId());
    }
}