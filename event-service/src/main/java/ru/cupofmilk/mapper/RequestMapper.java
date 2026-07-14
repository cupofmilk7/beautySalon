package ru.cupofmilk.mapper;

import ru.cupofmilk.dto.event.RequestDto;
import ru.cupofmilk.model.event.Request;

public class RequestMapper {
    public static RequestDto toDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setEvent(EventMapper.toDto(request.getEvent()));
        requestDto.setNow(request.isNow());

        return requestDto;
    }

    public static Request toModel(RequestDto requestDto) {
        Request request = new Request();
        request.setEvent(EventMapper.toModel(requestDto.getEvent()));
        request.setNow(requestDto.isNow());

        return request;
    }
}
