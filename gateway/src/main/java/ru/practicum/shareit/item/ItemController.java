package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid ItemDto itemDto,
                                             HttpServletRequest request) {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), itemDto);
        return itemClient.createItem(itemDto, userId);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable Long itemId,
                                              HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                      HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemClient.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                             HttpServletRequest request,
                                             @RequestParam String text) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemClient.searchItem(text, userId, from, size);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody ItemDto itemDto,
                                             HttpServletRequest request,
                                             @PathVariable Long itemId) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemClient.updateItem(itemId, itemDto, userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long itemId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        return itemClient.deleteItem(itemId);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid CommentDto commentDto,
                                                @PathVariable Long itemId,
                                                HttpServletRequest request) {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), commentDto);
        return itemClient.createComment(commentDto, itemId, userId);
    }
}