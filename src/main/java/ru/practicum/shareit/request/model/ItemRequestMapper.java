package ru.practicum.shareit.request.model;

import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestMapper {

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestorId(),
                itemRequest.getCreated(),
                new ArrayList<>()
        );
    }

    public ItemRequest toEntity(@Valid ItemRequestDto itemRequestDto) {
        if (itemRequestDto == null) {
            return null;
        }
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestorId(itemRequestDto.getRequester());
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public List<ItemRequestDto> mapToItemRequestDto(Iterable<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestList.add(toItemRequestDto(itemRequest));
        }
        return itemRequestList;
    }
}