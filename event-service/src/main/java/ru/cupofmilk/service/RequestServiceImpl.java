package ru.cupofmilk.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.cupofmilk.config.SmsConfig;
import ru.cupofmilk.dto.event.EventDto;
import ru.cupofmilk.dto.event.RequestDto;
import ru.cupofmilk.dto.user.UserDto;
import ru.cupofmilk.exception.NotFoundException;
import ru.cupofmilk.feign.EventClient;
import ru.cupofmilk.feign.UserClient;
import ru.cupofmilk.model.event.Event;
import ru.cupofmilk.model.event.Filter;
import ru.cupofmilk.model.event.Request;
import ru.cupofmilk.model.event.SmsRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    private final SmsConfig smsConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserClient userClient;
    private final EventClient eventClient;

    @PostConstruct
    public void init() {
        log.info("=== SMS Config loaded ===");
        log.info("SMS API Key: {}", smsConfig.getApiKey().subSequence(0, 10) + "*****");
        log.info("SMS Email: {}", smsConfig.getEmail());
        log.info("SMS Sign: {}", smsConfig.getSign());
    }

    @Override
    public RequestDto createRequest(RequestDto request) {
        try {
            EventDto eventDto;
            Filter filter;

            if (request.getEventId() != null) {
                ResponseEntity<EventDto> eventResponse = eventClient.getEvent(request.getEventId());
                eventDto = eventResponse.getBody();

                if (eventDto == null) {
                    throw new NotFoundException("Event not found with id: " + request.getEventId());
                }

                filter = eventDto.getFilter();
                if (filter == null) {
                    throw new NotFoundException("Filter not found for event: " + request.getEventId());
                }
            } else if (request.getEvent() != null) {
                eventDto = request.getEvent();
                filter = eventDto.getFilter();
            } else {
                throw new IllegalArgumentException("Either eventId or event must be provided");
            }

            if (eventDto.getDescription() == null || eventDto.getDescription().isEmpty()) {
                throw new IllegalArgumentException("Сообщение не может быть пустым");
            }

            List<UserDto> users = userClient.getUsers(
                    filter.getFirstDate().getYear(),
                    filter.getSecondDate().getYear()
            ).getBody();

            if (users == null || users.isEmpty()) {
                log.warn("No users found for the specified date range");
                return request;
            }

            String description = eventDto.getDescription();
            for (UserDto user : users) {
                SmsRequest smsRequest = new SmsRequest(
                        user.getNumber(),
                        description,
                        smsConfig.getSign()
                );

                sendSms(smsRequest);
            }

            // TODO: Сохранить запрос в БД для истории
            // smsRequestRepository.save(...);

            return request;

        } catch (Exception e) {
            log.error("Failed to send SMS: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка отправки SMS: " + e.getMessage());
        }
    }

    private void validateRequest(Request request) {

        Event event = request.getEvent();

        if (event.getDescription() == null || event.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Сообщение не может быть пустым");
        }
    }

    private String sendSms(SmsRequest request) {
        String url = "https://gate.smsaero.ru/v2/sms/send";

        Map<String, String> params = new HashMap<>();
        params.put("number", request.getPhoneNumber());
        params.put("text", request.getMessage());
        params.put("sign", smsConfig.getSign());

        String finalUrl = url + "?" + params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        HttpHeaders headers = new HttpHeaders();
        String auth = smsConfig.getEmail() + ":" + smsConfig.getApiKey();
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        log.info("Sending SMS to API: {}", finalUrl);
        log.info("Phone: {}, Message: {}, Sign: {}",
                request.getPhoneNumber(),
                request.getMessage(),
                request.getSign() != null ? request.getSign() : smsConfig.getSign());

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("SMS API response status: {}", response.getStatusCode());
            log.info("SMS API response body: {}", response.getBody());

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("SMS API вернул ошибку: " + response.getStatusCode() + ", body: " + response.getBody());
            }

            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling SMS API: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка вызова SMS API: " + e.getMessage());
        }
    }
}
