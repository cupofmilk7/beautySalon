package ru.cupofmilk.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsRequest {
    private String phoneNumber;

    private String message;

    private String sign;
}
