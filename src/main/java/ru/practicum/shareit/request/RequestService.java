package ru.practicum.shareit.request;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.model.ItemRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) throws ValidationException;

    List<ItemRequestDto> getRequestsByUser(Long userId);

    ItemRequestDto getRequestById(Long requestId, Long userId);

    List<ItemRequestDto> getRequestByAll(Long userId, Integer from, Integer size) throws ValidationException;
}