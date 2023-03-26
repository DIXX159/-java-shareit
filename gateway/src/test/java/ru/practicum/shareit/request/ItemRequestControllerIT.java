package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.model.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestClient requestClient;

    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new ItemRequestDto(1L,
                "desc",
                1L,
                LocalDateTime.now(),
                new ArrayList<>()
        );
    }

    @Test
    @SneakyThrows
    void createRequest_whenRequestCreated_thenReturnRequest() {
        when(requestClient.createRequest(any(), anyLong()))
                .thenReturn(ResponseEntity.ok(requestDto));

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester().intValue())));
    }

    @Test
    @SneakyThrows
    void createRequest_whenRequestIsNotValid_thenReturnBadRequest() {
        requestDto.setDescription("");

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @SneakyThrows
    void getRequestsByUser_whenInvoke_thenReturnOk() {
        Long userId = 1L;
        mockMvc.perform(get("/requests").header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestClient).getRequestsByUser(userId);
    }

    @Test
    @SneakyThrows
    void getRequestById_whenInvoke_thenReturnOk() {
        Long requestId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/requests/{requestId}", requestId).header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestClient).getRequestById(requestId, userId);
    }

    @Test
    @SneakyThrows
    void getRequestsByAll_whenInvoke_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestClient).getRequestByAll(userId, 0, 20);
    }
}