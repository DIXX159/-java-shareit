package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User createUser(User user) throws ValidationException, ThrowableException {
        if (user.getEmail() != null) {
            try {
                userRepository.save(user);
                return userRepository.findUserByEmail(user.getEmail());
            } catch (Throwable e) {
                throw new ThrowableException("Email уже зарегистрирован");
            }
        } else throw new ValidationException("Не указан e-mail");
    }

    @Override
    public User updateUser(Long userId, User user) throws ThrowableException {
        if (userRepository.findUserById(userId) != null) {
            user.setId(userId);
            User oldUser = userRepository.findUserById(userId);
            if (user.getEmail() == null || !userRepository.findAllEmail().contains(user.getEmail()) || Objects.equals(oldUser.getEmail(), user.getEmail())) {
                if (user.getEmail() == null) {
                    user.setEmail(oldUser.getEmail());
                }
                if (user.getName() == null) {
                    user.setName(oldUser.getName());
                }
                userRepository.updateUser(userId, user.getName(), user.getEmail());
                userRepository.saveAndFlush(user);
                return userRepository.findUserById(user.getId());
            } else throw new ThrowableException(Constants.emailExist);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public User getUserById(Long userId) {
        if (userRepository.findUserById(userId) != null) {
            return userRepository.findUserById(userId);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    @Override
    public void deleteUser(long userId) {
        if (userRepository.findUserById(userId) != null) {
            userRepository.deleteById(userId);
        } else throw new NotFoundException(Constants.userNotFound);
    }
}