package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto) throws ValidationException, ThrowableException;

    UserDto updateUser(Long userId, UserDto userDto) throws ValidationException, ThrowableException;

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteUser(long userId);
}