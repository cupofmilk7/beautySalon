package ru.cupofmilk.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {
    private EventDto event;
    private Long eventId;
    private boolean isNow;
}
