package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserClient userClient;

    private final UserDto userDto = new UserDto(
            1L,
            "UserName1",
            "username1@mail.ru");

    @Test
    @SneakyThrows
    void createUser() {
        when(userClient.createUser(userDto))
                .thenReturn(ResponseEntity.accepted().body(userDto));

        ResponseEntity<Object> response = userClient.createUser(userDto);

        assertEquals(userDto, response.getBody());
    }

    @Test
    @SneakyThrows
    void getUserById() {
        when(userClient.getUserById(userDto.getId()))
                .thenReturn(ResponseEntity.accepted().body(userDto));
        ResponseEntity<Object> response = userClient.getUserById(userDto.getId());

        assertEquals(userDto, response.getBody());
    }

    @Test
    void getAllUsers() {
        List<UserDto> expectedUsers = List.of(userDto);
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity.accepted().body(expectedUsers));

        ResponseEntity<Object> response = userClient.getAllUsers();

        assertEquals(expectedUsers, response.getBody());
    }

    @Test
    @SneakyThrows
    void updateUser() {
        UserDto userDto2 = new UserDto(
                null,
                "UserName2",
                "username2@mail.ru");
        when(userClient.updateUser(1L, userDto2))
                .thenReturn(ResponseEntity.accepted().body(userDto2));
        ResponseEntity<Object> response = userClient.updateUser(1L, userDto2);

        assertEquals(userDto2, response.getBody());
    }
}