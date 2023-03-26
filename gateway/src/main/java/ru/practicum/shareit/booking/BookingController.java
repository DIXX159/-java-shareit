package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.BookingDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody @Valid BookingDto bookingDto,
                                                HttpServletRequest request) {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), bookingDto);
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return bookingClient.createBooking(bookingDto, userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> bookingApproved(@PathVariable Long bookingId,
                                                  @RequestParam String approved,
                                                  HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return bookingClient.updateApproving(bookingId, userId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId,
                                                 HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByUser(@RequestParam(defaultValue = "all") String state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                   HttpServletRequest request) {
        BookingStatus stateParam = BookingStatus.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long userId = request.getIntHeader("X-Sharer-User-Id");
        return bookingClient.getAllBookingsByUserId(stateParam, userId, from, size);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getBookingItemsByUser(@RequestParam(defaultValue = "all") String state,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                        HttpServletRequest request) {
        BookingStatus stateParam = BookingStatus.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        long ownerId = request.getIntHeader("X-Sharer-User-Id");
        return bookingClient.getAllBookingsItemsByUserId(stateParam, ownerId, from, size);
    }
}