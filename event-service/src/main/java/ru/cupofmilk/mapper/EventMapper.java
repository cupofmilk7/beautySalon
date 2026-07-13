package ru.cupofmilk.mapper;

import ru.cupofmilk.model.event.Event;
import ru.cupofmilk.dto.event.EventDto;

public class EventMapper {
    public static EventDto toDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setDescription(event.getDescription());
        eventDto.setFirstDate(event.getFirstDate());
        eventDto.setSecondDate(event.getSecondDate());
        eventDto.setMessageDate(event.getMessageDate());
        return eventDto;
    }

    public static Event toModel(EventDto eventDto) {
        Event event = new Event();
        event.setId(eventDto.getId());
        event.setDescription(eventDto.getDescription());
        event.setFirstDate(eventDto.getFirstDate());
        event.setSecondDate(eventDto.getSecondDate());
        event.setMessageDate(eventDto.getMessageDate());
        return event;
    }
}
