package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking2Dto;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking2Dto createBooking(@RequestBody @Valid BookingDto bookingDto, HttpServletRequest request) throws ValidationException {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), bookingDto);
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public Booking2Dto bookingApproved(@PathVariable Long bookingId, @RequestParam String approved, HttpServletRequest request) throws ValidationException {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.updateApproving(bookingId, userId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public Booking2Dto getBookingById(@PathVariable Long bookingId, HttpServletRequest request) throws ValidationException {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<Booking2Dto> getBookingByUser(@RequestParam(required = false) String state,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                              HttpServletRequest request) throws ThrowableException {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return bookingService.getAllBookingsByUserId(state, userId, pageRequest);
    }

    @GetMapping(value = "/owner")
    public List<Booking2Dto> getBookingItemsByUser(@RequestParam(required = false) String state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                   HttpServletRequest request) throws ThrowableException {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long ownerId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.getAllBookingsItemsByUserId(ownerId, state, PageRequest.of(from, size));
    }
}