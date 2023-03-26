package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private final UserDto userDto1 = new UserDto(
            null,
            "UserName1",
            "username1@mail.ru");

    @Test
    @SneakyThrows
    void createUser() {
        when(userService.createUser(userDto1))
                .thenReturn(userDto1);

        UserDto response = userService.createUser(userDto1);

        assertEquals(response.getName(), userDto1.getName());
    }

    @Test
    @SneakyThrows
    void getUserById() {
        when(userService.getUserById(userDto1.getId()))
                .thenReturn(userDto1);
        UserDto response = userService.getUserById(userDto1.getId());

        assertEquals(userDto1, response);
    }

    @Test
    void getAllUsers() {
        List<UserDto> expectedUsers = List.of(new UserDto());
        when(userService.getAllUsers())
                .thenReturn(expectedUsers);

        List<UserDto> response = userService.getAllUsers();

        assertEquals(response, expectedUsers);
    }

    @Test
    @SneakyThrows
    void updateUser() {
        User expectedUsers = new User();
        expectedUsers.setName("UserName2");
        expectedUsers.setEmail("username2@mail.ru");
        UserDto userDto2 = new UserDto(
                null,
                "UserName2",
                "username2@mail.ru");
        when(userService.updateUser(1L, userDto2))
                .thenReturn(userDto2);
        UserDto response = userService.updateUser(1L, userDto2);

        assertEquals(userDto2.getName(), response.getName());
    }
}