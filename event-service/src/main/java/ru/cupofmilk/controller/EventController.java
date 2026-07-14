package ru.cupofmilk.controller;

import ru.cupofmilk.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cupofmilk.dto.event.EventDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventDto) {
        EventDto created = eventService.createEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable(name = "eventId") long eventId) {
        EventDto event = eventService.getEvent(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(
            @RequestParam(required = false, name = "date")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        }
        List<EventDto> events = eventService.getEvents(date);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/active")
    public ResponseEntity<List<EventDto>> getActiveEvents() {
        List<EventDto> events = eventService.getActiveEvents();
        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable(name = "eventId") long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable(name = "eventId") long eventId,
            @Valid @RequestBody EventDto eventDto) {
        EventDto updated = eventService.updateEvent(eventId, eventDto);
        return ResponseEntity.ok(updated);
    }
}