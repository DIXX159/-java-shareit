package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto userDto) throws ValidationException, ThrowableException {
        User user = userMapper.toEntity(userDto);
        if (user.getEmail() != null) {
            try {
                User save = userRepository.save(user);
                return userMapper.toUserDto(save);
            } catch (Throwable e) {
                throw new ThrowableException("Email уже зарегистрирован");
            }
        } else throw new ValidationException("Не указан e-mail");
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws ThrowableException {
        User user = userMapper.toEntity(userDto);
        if (userRepository.findUserById(userId) != null) {
            user.setId(userId);
            User oldUser = userRepository.findUserById(userId);
            List<String> emails = userRepository.findAllEmail();
            if (user.getEmail() == null || !emails.contains(user.getEmail()) || Objects.equals(oldUser.getEmail(), user.getEmail())) {
                if (user.getEmail() == null) {
                    user.setEmail(oldUser.getEmail());
                }
                if (user.getName() == null) {
                    user.setName(oldUser.getName());
                }
                userRepository.updateUser(userId, user.getName(), user.getEmail());
                return userMapper.toUserDto(userRepository.save(user));
            } else throw new ThrowableException(Constants.emailExist);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.mapToUserDto(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (userRepository.findUserById(userId) != null) {
            return userMapper.toUserDto(userRepository.findUserById(userId));
        } else throw new NotFoundException(Constants.userNotFound);
    }

    @Override
    public void deleteUser(long userId) {
        if (userRepository.findUserById(userId) != null) {
            userRepository.deleteById(userId);
        } else throw new NotFoundException(Constants.userNotFound);
    }
}