package ru.cupofmilk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cupofmilk.dto.UserDto;
import ru.cupofmilk.exception.NotFoundException;
import ru.cupofmilk.mapper.UserMapper;
import ru.cupofmilk.model.User;
import ru.cupofmilk.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toModel(userDto);
        User savedUser = userRepository.save(user);

        log.debug("User created with id: {}", savedUser.getId());

        return UserMapper.toDto(savedUser);
    }

    @Override
    public UserDto getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new NotFoundException("User not found with id: " + id);
                });

        log.debug("User found: {}", user);

        return UserMapper.toDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new NotFoundException("User not found with email: " + email);
                });

        log.debug("User found by email: {}", user);

        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsers(int yearOfBirthMin, int yearOfBirthMax) {

        if (yearOfBirthMin > yearOfBirthMax) {
            log.warn("Invalid date range: min={}, max={}", yearOfBirthMin, yearOfBirthMax);
            throw new IllegalArgumentException("yearOfBirthMin cannot be greater than yearOfBirthMax");
        }

        List<User> users = userRepository.findUsers(yearOfBirthMin, yearOfBirthMax);

        log.debug("Users found: {}", users);

        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public boolean deleteUser(Long id) {

        if (!checkUser(id)) return false;

        userRepository.deleteById(id);
        log.debug("User deleted: {}", id);

        return true;
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setNumber(userDto.getNumber());
        existingUser.setBirthday(userDto.getBirthday());

        User updatedUser = userRepository.save(existingUser);
        log.debug("User updated: {}", id);

        return UserMapper.toDto(updatedUser);
    }

    private boolean checkUser(Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            log.warn("User not found: {}", id);
            throw new NotFoundException("User not found with id: " + id);
        }
        return true;
    }
}
