package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public Item createItem(@RequestBody @Valid ItemDto itemDto, HttpServletRequest request) throws Throwable {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), itemDto);
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        Item item = itemMapper.toEntity(itemDto);
        return itemService.createItem(item, userId);
    }

    @GetMapping(value = "/{itemId}")
    public Item getItemById(@PathVariable Long itemId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getAllItemsByUserId(HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping(value = "/search")
    public List<Item> searchItem(HttpServletRequest request, @RequestParam String text) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.searchItem(text);
    }

    @PatchMapping(value = "/{itemId}")
    public Item updateItem(@RequestBody ItemDto itemDto, HttpServletRequest request, @PathVariable Long itemId) throws ValidationException, ThrowableException {
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        Item item = itemMapper.toEntity(itemDto);
        return itemService.updateItem(itemId, item, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteUser(@PathVariable long itemId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        itemService.deleteItem(itemId);
    }
}