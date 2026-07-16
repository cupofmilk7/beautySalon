package ru.cupofmilk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.cupofmilk.dto.event.EventDto;
import ru.cupofmilk.service.EventService;
import ru.cupofmilk.service.RequestService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private RequestService requestService;

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @Test
    void createEvent_returnsCreated() throws Exception {
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setDescription("Test Event");
        eventDto.setFirstDate(TEST_DATE);
        eventDto.setSecondDate(TEST_DATE.plusHours(1));

        when(eventService.createEvent(any(EventDto.class))).thenReturn(eventDto);

        String requestJson = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test Event"));

        verify(eventService, times(1)).createEvent(any(EventDto.class));
    }

    @Test
    void getEvent_returnsEvent() throws Exception {
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setDescription("Test Event");
        eventDto.setFirstDate(TEST_DATE);
        eventDto.setSecondDate(TEST_DATE.plusHours(1));

        when(eventService.getEvent(1L)).thenReturn(eventDto);

        mockMvc.perform(get("/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test Event"));

        verify(eventService, times(1)).getEvent(1L);
    }

    @Test
    void getEvents_returnsList() throws Exception {
        EventDto eventDto1 = new EventDto();
        eventDto1.setId(1L);
        eventDto1.setDescription("Test Event 1");
        eventDto1.setFirstDate(TEST_DATE);
        eventDto1.setSecondDate(TEST_DATE.plusHours(1));

        EventDto eventDto2 = new EventDto();
        eventDto2.setId(2L);
        eventDto2.setDescription("Test Event 2");
        eventDto2.setFirstDate(TEST_DATE);
        eventDto2.setSecondDate(TEST_DATE.plusHours(1));

        List<EventDto> eventDtos = Arrays.asList(eventDto1, eventDto2);

        when(eventService.getEvents(TEST_DATE)).thenReturn(eventDtos);

        mockMvc.perform(get("/events")
                        .param("date", "2023-01-01T12:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Test Event 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Test Event 2"));

        verify(eventService, times(1)).getEvents(TEST_DATE);
    }

    @Test
    void deleteEvent_returnsNoContent() throws Exception {
        when(eventService.deleteEvent(1L)).thenReturn(true);

        mockMvc.perform(delete("/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEvent(1L);
    }
}