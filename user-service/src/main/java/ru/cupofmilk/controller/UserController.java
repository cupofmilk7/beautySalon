package ru.cupofmilk.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cupofmilk.dto.user.UserDto;
import ru.cupofmilk.feign.UserClient;
import ru.cupofmilk.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserClient {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto created = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "userId") long userId) {
        UserDto user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{userEmail}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable(name = "userEmail") String userEmail) {
        UserDto user = userService.getUserByEmail(userEmail);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(name = "yearOfBirthMin", required = false, defaultValue = "1900") int yearOfBirthMin,
            @RequestParam(name = "yearOfBirthMax", required = false, defaultValue = "2100") int yearOfBirthMax) {
        List<UserDto> users = userService.getUsers(yearOfBirthMin, yearOfBirthMax);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "userId") long userId,
            @Valid @RequestBody UserDto userDto) {
        UserDto updated = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}