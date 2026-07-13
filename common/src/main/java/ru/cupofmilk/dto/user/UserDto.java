package ru.cupofmilk.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Некорректный номер телефона"
    )
    private String number;
}