package ru.cupofmilk.mapper;

import ru.cupofmilk.dto.event.RequestDto;
import ru.cupofmilk.model.event.Request;

public class RequestMapper {
    public static RequestDto toDto(Request request) {
        RequestDto requestDto = new RequestDto();
        if (request.getEvent() != null) {
            requestDto.setEvent(EventMapper.toDto(request.getEvent()));
        }
        requestDto.setEventId(request.getEventId());
        requestDto.setNow(request.isNow());
        return requestDto;
    }

    public static Request toModel(RequestDto requestDto) {
        Request request = new Request();
        if (requestDto.getEvent() != null) {
            request.setEvent(EventMapper.toModel(requestDto.getEvent()));
        }
        request.setEventId(requestDto.getEventId());
        request.setNow(requestDto.isNow());
        return request;
    }
}
