package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.model.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    RequestClient requestClient;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto(1L,
                "description",
                1L,
                LocalDateTime.now(),
                new ArrayList<>()
        );
    }

    @Test
    @SneakyThrows
    void createRequest() {
        when(requestClient.createRequest(itemRequestDto, 1L))
                .thenReturn(ResponseEntity.accepted().body(itemRequestDto));

        ResponseEntity<Object> response = requestClient.createRequest(itemRequestDto, 1L);

        assertEquals(itemRequestDto, response.getBody());
    }

    @Test
    @SneakyThrows
    void getRequestsByUser() {
        when(requestClient.getRequestsByUser(1L))
                .thenReturn(ResponseEntity.accepted().body(List.of(itemRequestDto)));

        ResponseEntity<Object> response = requestClient.getRequestsByUser(1L);

        assertEquals(List.of(itemRequestDto), response.getBody());
    }

    @Test
    void getRequestById() {
        when(requestClient.getRequestById(1L, 1L))
                .thenReturn(ResponseEntity.accepted().body(itemRequestDto));

        ResponseEntity<Object> response = requestClient.getRequestById(1L, 1L);

        assertEquals(itemRequestDto, response.getBody());
    }

    @Test
    @SneakyThrows
    void getRequestsByAll() {
        when(requestClient.getRequestByAll(1L, 0, 1))
                .thenReturn(ResponseEntity.accepted().body(List.of(itemRequestDto)));

        ResponseEntity<Object> response = requestClient.getRequestByAll(1L, 0, 1);

        assertEquals(List.of(itemRequestDto), response.getBody());
    }
}