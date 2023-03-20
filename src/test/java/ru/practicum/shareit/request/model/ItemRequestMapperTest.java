package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @InjectMocks
    private ItemRequestMapper itemRequestMapper;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    public void add() {
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("desc");
        itemRequest.setRequestorId(1L);
        itemRequest.setCreated(LocalDateTime.now());

        itemRequestDto = new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestorId(),
                itemRequest.getCreated(),
                new ArrayList<>()
        );
    }

    @Test
    void toItemRequestDto() {
        ItemRequestDto actualItemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequestDto.getId(), actualItemRequestDto.getId());
        assertEquals(itemRequestDto.getDescription(), actualItemRequestDto.getDescription());
        assertEquals(itemRequestDto.getRequester(), actualItemRequestDto.getRequester());
        assertEquals(itemRequestDto.getCreated(), actualItemRequestDto.getCreated());
        assertEquals(itemRequestDto.getItems(), actualItemRequestDto.getItems());
    }

    @Test
    void toEntity() {
        ItemRequest actualItemRequest = itemRequestMapper.toEntity(itemRequestDto);

        assertEquals(itemRequest.getDescription(), actualItemRequest.getDescription());
        assertEquals(itemRequest.getRequestorId(), actualItemRequest.getRequestorId());
        assertEquals(itemRequest.getCreated().withNano(0), actualItemRequest.getCreated().withNano(0));
    }

    @Test
    void toEntity_whenItemRequestDtoIsNull_thenReturnNull() {
        ItemRequest actualItemRequest = itemRequestMapper.toEntity(null);

        assertNull(actualItemRequest);
    }

    @Test
    void mapToItemRequestDto() {
        List<ItemRequestDto> actualItemRequestDtoList = itemRequestMapper.mapToItemRequestDto(List.of(itemRequest));

        assertEquals(itemRequestDto.getId(), actualItemRequestDtoList.get(0).getId());
        assertEquals(itemRequestDto.getDescription(), actualItemRequestDtoList.get(0).getDescription());
        assertEquals(itemRequestDto.getRequester(), actualItemRequestDtoList.get(0).getRequester());
        assertEquals(itemRequestDto.getCreated(), actualItemRequestDtoList.get(0).getCreated());
        assertEquals(itemRequestDto.getItems(), actualItemRequestDtoList.get(0).getItems());
    }
}