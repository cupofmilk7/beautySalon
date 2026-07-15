package ru.cupofmilk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
    private String apiKey;
    private String email;
    private String sign;
    private String apiUrl = "https://gate.smsaero.ru/v2/send";
}
