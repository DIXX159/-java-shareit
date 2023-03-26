package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @InjectMocks
    RequestServiceImpl requestService;

    @Mock
    RequestRepository requestRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;

    @Mock
    ItemRequestMapper itemRequestMapper;

    @Mock
    ItemMapper itemMapper;


    @Test
    @SneakyThrows
    void createRequest_whenItem() {
        ItemRequest expectedItemRequest = new ItemRequest();
        expectedItemRequest.setDescription("description");
        ItemRequestDto expectedItemRequestDto = itemRequestMapper.toItemRequestDto(expectedItemRequest);
        when(itemRequestMapper.toEntity(any()))
                .thenReturn(expectedItemRequest);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));

        ItemRequestDto actualRequest = requestService.createRequest(expectedItemRequestDto, 1L);

        assertEquals(expectedItemRequestDto, actualRequest);
        verify(requestRepository).save(expectedItemRequest);
    }

    @Test
    @SneakyThrows
    void createRequest_whenUserEmailNotFound_thenNotFoundExceptionThrown() {
        ItemRequest expectedItemRequest = new ItemRequest();
        ItemRequestDto expectedItemRequestDto = itemRequestMapper.toItemRequestDto(expectedItemRequest);
        when(userRepository.findById(1L))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> requestService.createRequest(expectedItemRequestDto, 1L));
        verify(requestRepository, never()).save(expectedItemRequest);
    }

    @Test
    @SneakyThrows
    void createRequest_whenItemDescriptionNotFound_thenValidationExceptionThrown() {
        ItemRequest expectedItemRequest = new ItemRequest();
        ItemRequestDto expectedItemRequestDto = new ItemRequestDto(null,
                "description",
                1L,
                LocalDateTime.now(),
                new ArrayList<>()
        );
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(itemRequestMapper.toEntity(any()))
                .thenReturn(expectedItemRequest);

        assertThrows(ValidationException.class, () -> requestService.createRequest(expectedItemRequestDto, 1L));
        verify(requestRepository, never()).save(expectedItemRequest);
    }

    @Test
    void getRequestsByUser() {
        List<ItemRequest> expectedItemRequest = List.of(new ItemRequest());
        List<ItemRequestDto> expectedItemRequestDto = itemRequestMapper.mapToItemRequestDto(expectedItemRequest);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));

        when(requestRepository.findItemRequestsByRequestorId(1L))
                .thenReturn(expectedItemRequest);

        List<ItemRequestDto> response = requestService.getRequestsByUser(1L);

        assertEquals(response, expectedItemRequestDto);
    }

    @Test
    @SneakyThrows
    void getRequestsByUser_whenUserEmailNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(1L))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> requestService.getRequestsByUser(1L));
    }

    @Test
    void getRequestById() {
        ItemRequest expectedItemRequest = new ItemRequest();
        expectedItemRequest.setId(1L);
        expectedItemRequest.setRequestorId(1L);
        expectedItemRequest.setDescription("desc");
        expectedItemRequest.setCreated(LocalDateTime.now());

        ItemRequestDto expectedItemRequestDto = new ItemRequestDto(
                expectedItemRequest.getId(),
                expectedItemRequest.getDescription(),
                expectedItemRequest.getRequestorId(),
                expectedItemRequest.getCreated(),
                new ArrayList<>());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(requestRepository.findItemRequestById(1L))
                .thenReturn(expectedItemRequest);
        when(itemRequestMapper.toItemRequestDto(any()))
                .thenReturn(expectedItemRequestDto);

        when(itemMapper.mapToItemDto(any()))
                .thenReturn(List.of(new ItemDto()));
        when(itemRepository.findItemsByRequestId(1L))
                .thenReturn(List.of(new Item()));

        ItemRequestDto response = requestService.getRequestById(1L, 1L);

        assertEquals(response, expectedItemRequestDto);
    }

    @Test
    @SneakyThrows
    void getRequestById_whenUserEmailNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(1L))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> requestService.getRequestById(1L, 1L));
    }

    @Test
    @SneakyThrows
    void getRequestById_whenItemNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(requestRepository.findItemRequestById(1L))
                .thenThrow(new NotFoundException(Constants.RequestNotFound));

        assertThrows(NotFoundException.class, () -> requestService.getRequestById(1L, 1L));
    }

    @Test
    void getRequestByAll() {
        List<ItemRequest> expectedItemRequest = List.of(new ItemRequest());
        List<ItemRequestDto> expectedItemRequestDto = itemRequestMapper.mapToItemRequestDto(expectedItemRequest);

        when(requestRepository.findItemRequestsByRequestorIdIsNot(1L, PageRequest.of(0, 1, Sort.by("created"))))
                .thenReturn(expectedItemRequest);
        List<ItemRequestDto> response = requestService.getRequestByAll(1L, 0, 1);

        assertEquals(response, expectedItemRequestDto);
    }
}