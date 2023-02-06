package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user) throws ValidationException, ThrowableException;

    User updateUser(Long userId, User user) throws ValidationException, ThrowableException;

    List<User> getAllUsers();

    User getUserById(Long id);

    void deleteUser(long userId);
}