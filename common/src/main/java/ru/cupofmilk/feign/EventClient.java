package ru.cupofmilk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.cupofmilk.dto.event.EventDto;

@FeignClient(name = "event-service", url = "http://localhost:8082")
public interface EventClient {
    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable(name = "eventId") long eventId);
}
