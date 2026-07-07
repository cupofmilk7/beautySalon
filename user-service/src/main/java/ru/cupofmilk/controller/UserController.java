package ru.cupofmilk.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.cupofmilk.dto.UserDto;
import ru.cupofmilk.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable(name = "userId") long userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/email/{userEmail}")
    public UserDto getUserByEmail(@PathVariable(name = "userEmail") String userEmail) {
        return userService.getUserByEmail(userEmail);
    }

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(name = "yearOfBirthMin", required = false, defaultValue = "1900") int yearOfBirthMin,
            @RequestParam(name = "yearOfBirthMax", required = false, defaultValue = "2100") int yearOfBirthMax) {
        return userService.getUsers(yearOfBirthMin, yearOfBirthMax);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(
            @PathVariable(name = "userId") long userId,
            @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public boolean deleteUser(@PathVariable(name = "userId") long userId) {
        return userService.deleteUser(userId);
    }
}