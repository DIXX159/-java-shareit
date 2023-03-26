package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) throws ValidationException {
        ItemRequest itemRequest = itemRequestMapper.toEntity(itemRequestDto);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.userNotFound));
        if (itemRequest.getDescription() != null) {
            itemRequest.setRequestorId(userId);
            return itemRequestMapper.toItemRequestDto(requestRepository.save(itemRequest));
        } else throw new ValidationException("Нет описания");
    }

    @Override
    public List<ItemRequestDto> getRequestsByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.userNotFound));
        List<ItemRequestDto> itemRequestDtos = itemRequestMapper.mapToItemRequestDto(requestRepository.findItemRequestsByRequestorId(userId));
        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            itemRequestDto.setItems(itemMapper.mapToItemDto(itemRepository.findItemsByRequestId(itemRequestDto.getId())));
        }
        return itemRequestDtos;
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.userNotFound));
        try {
            ItemRequest itemRequestById = requestRepository.findItemRequestById(requestId);
            ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequestById);

            itemRequestDto.setItems(itemMapper.mapToItemDto(itemRepository.findItemsByRequestId(itemRequestDto.getId())));
            return itemRequestDto;
        } catch (Throwable e) {
            throw new NotFoundException(Constants.RequestNotFound);
        }
    }

    @Override
    public List<ItemRequestDto> getRequestByAll(Long userId, Integer from, Integer size) {
        List<ItemRequest> itemRequests = requestRepository.findItemRequestsByRequestorIdIsNot(userId, PageRequest.of(from, size, Sort.by("created")));
        List<ItemRequestDto> itemRequestDtos = itemRequestMapper.mapToItemRequestDto(itemRequests);
        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            itemRequestDto.setItems(itemMapper.mapToItemDto(itemRepository.findItemsByRequestId(itemRequestDto.getId())));
        }
        return itemRequestDtos;
    }
}