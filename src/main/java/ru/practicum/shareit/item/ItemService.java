package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Item item, Long userId) throws Throwable;

    ItemDto updateItem(Long itemId, ItemDto item, Long userId) throws ValidationException, ThrowableException;

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> getAllItems();

    ItemDto getItemById(Long id, Long userId);

    List<ItemDto> searchItem(String text);

    void deleteItem(long userId);

    CommentDto createComment(Comment comment, Long itemId, Long userId) throws ValidationException;
}