package ru.cupofmilk.service;

import org.springframework.stereotype.Service;
import ru.cupofmilk.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(Long id);

    UserDto getUserByEmail(String email);

    List<UserDto> getUsers(
            int yearOfBirthMin,
            int yearOfBirthMax
    );

    boolean deleteUser(Long id);

    UserDto updateUser(Long id, UserDto userDto);
}
