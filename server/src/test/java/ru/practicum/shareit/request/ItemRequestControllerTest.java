package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    RequestService requestService;

    private final ItemRequest itemRequest = new ItemRequest();
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequest.setDescription("description");
        itemRequestDto = new ItemRequestDto(null,
                "description",
                1L,
                LocalDateTime.now(),
                new ArrayList<>()
        );
    }

    @Test
    @SneakyThrows
    void createRequest() {
        when(requestService.createRequest(itemRequestDto, 1L))
                .thenReturn(itemRequestDto);

        ItemRequestDto response = requestService.createRequest(itemRequestDto, 1L);

        assertEquals(response.getDescription(), itemRequest.getDescription());
    }

    @Test
    @SneakyThrows
    void getRequestsByUser() {
        when(requestService.getRequestsByUser(1L))
                .thenReturn(List.of(itemRequestDto));

        List<ItemRequestDto> response = requestService.getRequestsByUser(1L);

        assertEquals(response.get(0).getDescription(), itemRequest.getDescription());
    }

    @Test
    void getRequestById() {
        when(requestService.getRequestById(1L, 1L))
                .thenReturn(itemRequestDto);

        ItemRequestDto response = requestService.getRequestById(1L, 1L);

        assertEquals(response.getDescription(), itemRequest.getDescription());
    }

    @Test
    @SneakyThrows
    void getRequestsByAll() {
        when(requestService.getRequestByAll(1L, 0, 1))
                .thenReturn(List.of(itemRequestDto));

        List<ItemRequestDto> response = requestService.getRequestByAll(1L, 0, 1);

        assertEquals(response.get(0).getDescription(), itemRequest.getDescription());
    }
}