package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                                HttpServletRequest request) {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), itemRequestDto);
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return requestClient.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return requestClient.getRequestsByUser(userId);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId,
                                                 HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return requestClient.getRequestById(requestId, userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getRequestsByAll(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                   HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return requestClient.getRequestByAll(userId, from, size);
    }
}