package ru.cupofmilk.dto.event;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.cupofmilk.model.event.Filter;

import java.time.LocalDateTime;

@Data
public class EventDto {

    private Long id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    private LocalDateTime firstDate;

    private LocalDateTime secondDate;

    private Filter filter;

    private LocalDateTime messageDate;
}
