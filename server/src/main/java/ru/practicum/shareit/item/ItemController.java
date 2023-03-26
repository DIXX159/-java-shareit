package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.ItemDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid ItemDto itemDto, HttpServletRequest request) throws Throwable {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), itemDto);
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return itemService.createItem(itemDto, userId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                             HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchItem(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                    HttpServletRequest request, @RequestParam String text) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemService.searchItem(text, from, size);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, HttpServletRequest request, @PathVariable Long itemId) throws ValidationException, ThrowableException {
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteUser(@PathVariable long itemId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        itemService.deleteItem(itemId);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto createComment(@RequestBody @Valid CommentDto commentDto, @PathVariable Long itemId, HttpServletRequest request) throws Throwable {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), commentDto);
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return itemService.createComment(commentDto, itemId, userId);
    }
}