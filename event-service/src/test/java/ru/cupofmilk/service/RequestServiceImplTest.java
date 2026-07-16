package ru.cupofmilk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.cupofmilk.config.SmsConfig;
import ru.cupofmilk.dto.event.EventDto;
import ru.cupofmilk.dto.event.RequestDto;
import ru.cupofmilk.dto.user.UserDto;
import ru.cupofmilk.feign.EventClient;
import ru.cupofmilk.feign.UserClient;
import ru.cupofmilk.model.event.Filter;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private SmsConfig smsConfig;

    @Mock
    private UserClient userClient;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private RequestServiceImpl requestService;

    private RequestDto requestDto;
    private EventDto eventDto;
    private Filter filter;
    private UserDto userDto;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

        filter = new Filter();
        filter.setFirstDate(testDate);
        filter.setSecondDate(testDate.plusDays(1));

        eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setDescription("Test Event");
        eventDto.setFilter(filter);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setNumber("+79111234567");

        requestDto = new RequestDto();
        requestDto.setEventId(1L);
    }

    @Test
    void createRequest_missingEventData_throwsException() {
        requestDto.setEventId(null);
        requestDto.setEvent(null);

        // Исправлено: проверяем RuntimeException, так как метод оборачивает все исключения
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> requestService.createRequest(requestDto));
        assertTrue(exception.getMessage().contains("Either eventId or event must be provided"));
    }

    @Test
    void createRequest_emptyMessage_throwsException() {
        when(eventClient.getEvent(1L)).thenReturn(ResponseEntity.ok(eventDto));
        eventDto.setDescription("");

        requestDto.setEventId(1L);

        // Исправлено: проверяем RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> requestService.createRequest(requestDto));
        assertTrue(exception.getMessage().contains("Сообщение не может быть пустым"));
    }

    @Test
    void createRequest_noUsersFound() {
        when(eventClient.getEvent(1L)).thenReturn(ResponseEntity.ok(eventDto));
        when(userClient.getUsers(2023, 2023)).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        RequestDto result = requestService.createRequest(requestDto);

        assertNotNull(result);
        assertEquals(requestDto.getEventId(), result.getEventId());
    }

    @Test
    void createRequest_eventNotFound_throwsException() {
        when(eventClient.getEvent(1L)).thenReturn(ResponseEntity.notFound().build());

        // Исправлено: проверяем RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> requestService.createRequest(requestDto));
        assertTrue(exception.getMessage().contains("Event not found with id: 1"));
    }

    @Test
    void createRequest_filterNotFound_throwsException() {
        eventDto.setFilter(null);
        when(eventClient.getEvent(1L)).thenReturn(ResponseEntity.ok(eventDto));

        // Исправлено: проверяем RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> requestService.createRequest(requestDto));
        assertTrue(exception.getMessage().contains("Filter not found for event: 1"));
    }
}