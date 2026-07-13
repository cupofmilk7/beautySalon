package ru.cupofmilk.controller;

import ru.cupofmilk.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.cupofmilk.dto.event.EventDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @PostMapping()
    public EventDto createEvent(@Valid @RequestBody EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @GetMapping("/{eventId}")
    public EventDto getEvent(@PathVariable(name = "eventId") long eventId) {
        return eventService.getEvent(eventId);
    }

    @GetMapping()
    public List<EventDto> getEvents(@RequestParam(required = false, name = "date")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date)
    {
        if (date == null) {
            date = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        }

        return eventService.getEvents(date);
    }

    @GetMapping("/active")
    public List<EventDto> getActiveEvents()
    {
        return eventService.getActiveEvents();
    }

    @DeleteMapping("/{eventId}")
    public boolean deleteEvent(@PathVariable(name = "eventId") long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @PutMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable(name = "eventId") long eventId,
                                @Valid @RequestBody EventDto eventDto) {
        return eventService.updateEvent(eventId, eventDto);
    }
}
