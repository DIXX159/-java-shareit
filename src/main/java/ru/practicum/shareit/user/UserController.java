package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto, HttpServletRequest request) throws ValidationException, ThrowableException {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), userDto);
        User user = userMapper.toEntity(userDto);
        return userService.createUser(user);
    }

    @GetMapping(value = "/{userId}")
    public User getUserById(@PathVariable Long userId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers(HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        final List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @PatchMapping(value = "/{userId}")
    public User updateUser(@RequestBody @Valid UserDto userDto, HttpServletRequest request, @PathVariable Long userId) throws ValidationException, ThrowableException {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), userDto);
        User user = userMapper.toEntity(userDto);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        userService.deleteUser(userId);
    }
}