package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    @PostMapping
    public Booking createBooking(@RequestBody @Valid BookingDto bookingDto, HttpServletRequest request) {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), bookingDto);
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        Booking booking = bookingMapper.toEntity(bookingDto);
        return bookingService.createBooking(booking, userId);
    }

    @PatchMapping(value = "/{bookingId}?approved={approved}")
    public Booking bookingApproved(@PathVariable Long bookingId, HttpServletRequest request) {
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.updateApproving(bookingId, userId);
    }

    @GetMapping(value = "/{bookingId}")
    public Booking getBookingById(@PathVariable Long bookingId, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping(value = "?state={state}")
    public List<Booking> getBookingByUser(@PathVariable String state, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.getAllBookingsByUserId(state, userId);
    }

    @GetMapping(value = "/owner?state={state}")
    public List<Booking> getBookingItemsByUser(@PathVariable Long ownerId, @PathVariable String state, HttpServletRequest request) {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.getAllBookingsItemsByUserId(ownerId, state);
    }
}