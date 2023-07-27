package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
    private RequestServiceImpl requestService;

    @Test
    @SneakyThrows
    void createRequest_whenRequestCreated_thenReturnRequest() {
        ItemRequestDto requestDto = new ItemRequestDto(null, "desc", 1L, LocalDateTime.now(), new ArrayList<>());
        when(requestService.createRequest(any(), anyLong()))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
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
        ItemRequestDto requestDto = new ItemRequestDto(null, "", 1L, LocalDateTime.now(), new ArrayList<>());

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
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

        verify(requestService).getRequestsByUser(userId);
    }

    @Test
    @SneakyThrows
    void getRequestById_whenInvoke_thenReturnOk() {
        Long requestId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/requests/{requestId}", requestId).header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestService).getRequestById(requestId, userId);
    }

    @Test
    @SneakyThrows
    void getRequestsByAll_whenInvoke_thenReturnOk() {
        Long userId = 1L;
        mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestService).getRequestByAll(userId, 0, 20);
    }
}