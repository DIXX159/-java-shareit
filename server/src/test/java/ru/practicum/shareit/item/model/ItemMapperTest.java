package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking2ItemDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @InjectMocks
    ItemMapper itemMapper;

    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    public void add() {
        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(1L);
        item.setRequestId(1L);

        itemDto = new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId(),
                new Booking2ItemDto(),
                new Booking2ItemDto(),
                new ArrayList<>()
        );
    }

    @Test
    void toItemDto() {
        ItemDto actualItemDto = itemMapper.toItemDto(item);

        assertEquals(itemDto.getId(), actualItemDto.getId());
        assertEquals(itemDto.getName(), actualItemDto.getName());
        assertEquals(itemDto.getDescription(), actualItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), actualItemDto.getAvailable());
        assertEquals(itemDto.getOwner(), actualItemDto.getOwner());
        assertEquals(itemDto.getRequestId(), actualItemDto.getRequestId());
    }

    @Test
    void toEntity() {
        Item actualItem = itemMapper.toEntity(itemDto);

        assertEquals(item.getName(), actualItem.getName());
        assertEquals(item.getDescription(), actualItem.getDescription());
        assertEquals(item.getAvailable(), actualItem.getAvailable());
        assertEquals(item.getOwner(), actualItem.getOwner());
        assertEquals(item.getRequestId(), actualItem.getRequestId());
    }

    @Test
    void toEntity_whenCommentDtoIsNull_thenReturnNull() {
        Item actualItem = itemMapper.toEntity(null);

        assertNull(actualItem);
    }

    @Test
    void mapToItemDto() {
        List<ItemDto> itemDtoList = itemMapper.mapToItemDto(List.of(item));

        assertEquals(itemDto.getId(), itemDtoList.get(0).getId());
        assertEquals(itemDto.getName(), itemDtoList.get(0).getName());
        assertEquals(itemDto.getDescription(), itemDtoList.get(0).getDescription());
        assertEquals(itemDto.getAvailable(), itemDtoList.get(0).getAvailable());
        assertEquals(itemDto.getOwner(), itemDtoList.get(0).getOwner());
        assertEquals(itemDto.getRequestId(), itemDtoList.get(0).getRequestId());
    }
}