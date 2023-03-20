package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    @SneakyThrows
    void createUser_whenUserCreated_thenSavedUser() {
        User expectedUser = new User();
        expectedUser.setEmail("mail@mail.ru");
        UserDto expectedUserDto = userMapper.toUserDto(expectedUser);
        when(userMapper.toEntity(expectedUserDto))
                .thenReturn(expectedUser);
        when(userRepository.save(expectedUser))
                .thenReturn(expectedUser);

        UserDto actualUser = userServiceImpl.createUser(expectedUserDto);

        assertEquals(expectedUserDto, actualUser);
        verify(userRepository).save(expectedUser);
    }

    @Test
    @SneakyThrows
    void createUser_whenUserEmailExist_thenThrowableExceptionThrown() {
        User expectedUser = new User();
        expectedUser.setEmail("mail@mail.ru");

        UserDto expectedUserDto = new UserDto();
        when(userMapper.toEntity(expectedUserDto))
                .thenReturn(expectedUser);
        when(userRepository.save(any()))
                .thenThrow(new ConstraintViolationException("qwe", new SQLException(), "asd"));

        assertThrows(ThrowableException.class, () -> userServiceImpl.createUser(expectedUserDto));
    }

    @Test
    @SneakyThrows
    void createUser_whenUserEmailNotFound_thenValidationExceptionThrown() {
        User expectedUser = new User();
        UserDto expectedUserDto = new UserDto();
        when(userMapper.toEntity(expectedUserDto))
                .thenReturn(expectedUser);

        assertThrows(ValidationException.class, () -> userServiceImpl.createUser(expectedUserDto));
        verify(userRepository, never()).save(expectedUser);
    }

    @Test
    @SneakyThrows
    void updateUser_whenUserUpdated_thenUpdateUser() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setName("name2");
        expectedUser.setEmail("mail2@mail.ru");

        UserDto updatedUser = new UserDto(null, "name2", "mail2@mail.ru");
        when(userMapper.toEntity(updatedUser))
                .thenReturn(expectedUser);
        when(userRepository.findUserById(userId))
                .thenReturn(expectedUser);

        userServiceImpl.updateUser(userId, updatedUser);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("name2", savedUser.getName());
        assertEquals("mail2@mail.ru", savedUser.getEmail());
    }

    @Test
    void updateUser_whenUserNotFound_thenUserNotFoundExceptionThrown() {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setName("name2");
        updatedUser.setEmail("mail2@mail.ru");

        when(userRepository.findUserById(userId))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> userServiceImpl.updateUser(userId, any()));
        verify(userRepository, never()).updateUser(any(), any(), any());
    }

    @Test
    void updateUser_whenUserEmailExist_thenThrowableExceptionThrown() {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setName("name1");
        oldUser.setEmail("mail@mail.ru");
        UserDto updatedUserDto = new UserDto(null, "name2", "mail2@mail.ru");
        User updatedUser = new User();
        updatedUser.setName("name2");
        updatedUser.setEmail("mail2@mail.ru");
        when(userMapper.toEntity(updatedUserDto))
                .thenReturn(updatedUser);
        when(userRepository.findUserById(userId))
                .thenReturn(oldUser);
        when(userRepository.findAllEmail())
                .thenReturn(List.of("mail2@mail.ru"));

        assertThrows(ThrowableException.class, () -> userServiceImpl.updateUser(userId, updatedUserDto));
        verify(userRepository, never()).updateUser(any(), any(), any());
    }

    @Test
    void getAllUsers() {
        List<UserDto> expectedUsers = List.of(new UserDto());
        when(userServiceImpl.getAllUsers())
                .thenReturn(expectedUsers);

        List<UserDto> response = userServiceImpl.getAllUsers();

        assertEquals(response, expectedUsers);
    }

    @Test
    void getUserById_WhenUserFound_ThenReturnedUser() {
        Long userId = 1L;
        User expectedUser = new User();
        UserDto expectedUserDto = userMapper.toUserDto(expectedUser);
        when(userRepository.findUserById(userId))
                .thenReturn(expectedUser);

        UserDto actualUser = userServiceImpl.getUserById(userId);

        assertEquals(expectedUserDto, actualUser);
    }

    @Test
    void getUserById_whenUserNotFound_thenUserNotFoundExceptionThrown() {
        Long userId = 1L;
        when(userRepository.findUserById(userId))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> userServiceImpl.getUserById(userId));
    }

    @Test
    void deleteUser_whenUserNotFound_thenUserNotFoundExceptionThrown() {
        doThrow(NotFoundException.class).when(userRepository).findUserById(1L);

        assertThrows(NotFoundException.class, () -> userServiceImpl.deleteUser(1L));

        verify(userRepository, never()).deleteById(1L);
    }
}