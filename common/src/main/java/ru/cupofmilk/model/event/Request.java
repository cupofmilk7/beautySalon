package ru.cupofmilk.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {
    private Event event;
    private Long eventId;
    private boolean isNow;
}
