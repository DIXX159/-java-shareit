package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId() != null ? item.getRequestId() : null,
                null,
                null,
                new ArrayList<>()
        );
    }

    public Item toEntity(@Valid ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(itemDto.getOwner());
        item.setAvailable(itemDto.getAvailable());
        item.setRequestId(itemDto.getRequestId());
        return item;
    }

    public List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item : items) {
            itemDto.add(toItemDto(item));
        }
        return itemDto;
    }
}