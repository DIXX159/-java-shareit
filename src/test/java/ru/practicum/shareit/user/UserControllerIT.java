package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.UserDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @SneakyThrows
    void createUser_whenUserCreated_thenReturnUser() {
        UserDto userDto = new UserDto(null, "name", "name@mail.ru");
        when(userService.createUser(any()))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    @SneakyThrows
    void createUser_whenUserIsNotValid_thenReturnBadRequest() {
        UserDto userDto = new UserDto(null, "name", "mail.ru");

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getUserById_whenInvoke_thenReturnOk() {
        Long userId = 1L;
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).getUserById(userId);
    }


    @Test
    @SneakyThrows
    void getAllUsers_whenInvoke_thenReturnOk() {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getAllUsers();
    }

    @Test
    @SneakyThrows
    void updateUser_whenUserIsUpdated_thenUserSave() {
        Long userId = 1L;
        UserDto userDto = new UserDto(null, "name2", "name2@mail.ru");
        when(userService.updateUser(anyLong(), any()))
                .thenReturn(userDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService).updateUser(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void updateUser_whenUserIsNotValid_thenReturnedBadRequest() {
        Long userId = 1L;
        UserDto userDto1 = new UserDto(null, "name", "mail.ru");

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto1)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(userId, userDto1);
    }

    @Test
    @SneakyThrows
    void deleteUser_whenInvoke_thenReturnOk() {
        mockMvc.perform(delete("/users/{userId}", 1))
                .andExpect(status().isOk());

        verify(userService).deleteUser(anyLong());
    }
}