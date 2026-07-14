package ru.cupofmilk.service;

import org.springframework.stereotype.Service;
import ru.cupofmilk.dto.event.RequestDto;

@Service
public interface RequestService {
    RequestDto createRequest(RequestDto request);
}
