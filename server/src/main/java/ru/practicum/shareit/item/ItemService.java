package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemdto, Long userId) throws Throwable;

    ItemDto updateItem(Long itemId, ItemDto itemdto, Long userId) throws ValidationException, ThrowableException;

    List<ItemDto> getAllItemsByUserId(Long userId, Integer from, Integer size);

    List<ItemDto> getAllItems();

    ItemDto getItemById(Long id, Long userId);

    List<ItemDto> searchItem(String text, Integer from, Integer size);

    void deleteItem(long userId);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) throws ValidationException;
}