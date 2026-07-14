package ru.cupofmilk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cupofmilk.dto.event.RequestDto;

@RequiredArgsConstructor
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public RequestDto createRequest(RequestDto request) {
        return null; // todo реализация отправки
    }
}
