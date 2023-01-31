package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface UserService {
    User createUser(User user) throws ValidationException;

    User updateUser(Long userId, User user) throws ValidationException;

    List<User> getAllUsers();

    User getUserById(Long id);

    void deleteUser(long userId);
}