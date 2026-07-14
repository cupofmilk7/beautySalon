package ru.cupofmilk.service;

import ru.cupofmilk.mapper.EventMapper;
import ru.cupofmilk.model.event.Event;
import ru.cupofmilk.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cupofmilk.dto.event.EventDto;
import ru.cupofmilk.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private static final LocalDateTime DEFAULT_START_DATE = LocalDateTime.of(1900, 1, 1, 0, 0, 0);

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        Event event = EventMapper.toModel(eventDto);
        Event savedEvent = eventRepository.save(event);

        log.debug("Event created with id: {}", savedEvent.getId());

        return EventMapper.toDto(savedEvent);
    }

    @Override
    public EventDto getEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.warn("Event not found with id: {}", eventId);
                    return new NotFoundException("Event not found with id: " + eventId);
                });

        log.debug("Event found: {}", event);

        return EventMapper.toDto(event);
    }

    @Override
    public List<EventDto> getEvents(LocalDateTime date) {
        if (date == null) {
            date = DEFAULT_START_DATE;
        }

        List<Event> events = eventRepository.findByFirstDateAfter(date);

        log.debug("Found {} events from date: {}", events.size(), date);

        return events.stream()
                .map(EventMapper::toDto)
                .toList();
    }

    @Override
    public List<EventDto> getActiveEvents() {
        LocalDateTime now = LocalDateTime.now();

        List<Event> activeEvents = eventRepository.findActiveEvents(now);

        log.debug("Found {} active events", activeEvents.size());

        return activeEvents.stream()
                .map(EventMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteEvent(long eventId) {
        if (!checkEvent(eventId)) {
            return false;
        }

        eventRepository.deleteById(eventId);
        log.debug("Event deleted: {}", eventId);

        return true;
    }

    @Override
    @Transactional
    public EventDto updateEvent(long eventId, EventDto eventDto) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.warn("Event not found for update with id: {}", eventId);
                    return new NotFoundException("Event not found with id: " + eventId);
                });

        if (eventDto.getDescription() != null) {
            existingEvent.setDescription(eventDto.getDescription());
        }
        if (eventDto.getFirstDate() != null) {
            existingEvent.setFirstDate(eventDto.getFirstDate());
        }
        if (eventDto.getSecondDate() != null) {
            existingEvent.setSecondDate(eventDto.getSecondDate());
        }
        if (eventDto.getMessageDate() != null) {
            existingEvent.setMessageDate(eventDto.getMessageDate());
        }

        Event updatedEvent = eventRepository.save(existingEvent);
        log.debug("Event updated: {}", eventId);

        return EventMapper.toDto(updatedEvent);
    }

    private boolean checkEvent(long eventId) {
        boolean exists = eventRepository.existsById(eventId);
        if (!exists) {
            log.warn("Event not found: {}", eventId);
            throw new NotFoundException("Event not found with id: " + eventId);
        }
        return true;
    }
}