package ru.cupofmilk.service;

import ru.cupofmilk.dto.event.EventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventDto createEvent(EventDto eventDto);

    EventDto getEvent(long eventId);

    List<EventDto> getEvents(LocalDateTime date);

    List<EventDto> getActiveEvents();

    boolean deleteEvent(long eventId);

    EventDto updateEvent(long eventId, EventDto eventDto);
}
