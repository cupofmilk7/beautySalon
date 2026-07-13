package ru.cupofmilk.mapper;

import ru.cupofmilk.dto.user.UserDto;
import ru.cupofmilk.model.user.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setBirthday(user.getBirthday());
        userDto.setEmail(user.getEmail());
        userDto.setNumber(user.getNumber());
        return userDto;
    }

    public static User toModel(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setBirthday(userDto.getBirthday());
        user.setEmail(userDto.getEmail());
        user.setNumber(userDto.getNumber());
        return user;
    }
}
