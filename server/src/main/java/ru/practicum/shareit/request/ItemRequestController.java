package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.model.ItemRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestBody @Valid ItemRequestDto itemRequestDto, HttpServletRequest request) throws ValidationException {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), itemRequestDto);
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return requestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsByUser(HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return requestService.getRequestsByUser(userId);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Long requestId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return requestService.getRequestById(requestId, userId);
    }

    @GetMapping(value = "/all")
    public List<ItemRequestDto> getRequestsByAll(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                 HttpServletRequest request) throws ValidationException {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return requestService.getRequestByAll(userId, from, size);
    }
}