package ru.cupofmilk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cupofmilk.dto.user.UserDto;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {
    @GetMapping("/users")
    ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(name = "yearOfBirthMin", required = false, defaultValue = "1900") int yearOfBirthMin,
            @RequestParam(name = "yearOfBirthMax", required = false, defaultValue = "2100") int yearOfBirthMax
    );
}