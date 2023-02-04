package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item, Long userId) throws Throwable;

    Item updateItem(Long itemId, Item item, Long userId) throws ValidationException, ThrowableException;

    List<Item> getAllItemsByUserId(Long userId);

    List<Item> getAllItems();

    Item getItemById(Long id);

    List<Item> searchItem(String text);

    void deleteItem(long userId);
}