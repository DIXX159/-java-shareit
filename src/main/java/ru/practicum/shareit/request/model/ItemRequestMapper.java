package ru.practicum.shareit.request.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.BookingDto;

import javax.validation.Valid;

@Component
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestorId()
        );
    }

    public ItemRequest toEntity(@Valid ItemRequestDto itemRequestDto) {
        if (itemRequestDto == null) {
            return null;
        }
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestorId(itemRequestDto.getRequestor());
        return itemRequest;
    }
}