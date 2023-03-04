package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking2Dto;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;

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
    public Booking2Dto createBooking(@RequestBody @Valid BookingDto bookingDto, HttpServletRequest request) throws ValidationException {
        log.debug("Получен {} запрос {} тело запроса: {}", request.getMethod(), request.getRequestURI(), bookingDto);
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        Booking booking = bookingMapper.toEntity(bookingDto);
        return bookingService.createBooking(booking, userId);
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
    public List<Booking2Dto> getBookingByUser(@RequestParam(required = false) String state, HttpServletRequest request) throws ThrowableException {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long userId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.getAllBookingsByUserId(state, userId);
    }

    @GetMapping(value = "/owner")
    public List<Booking2Dto> getBookingItemsByUser(@RequestParam(required = false) String state, HttpServletRequest request) throws ThrowableException {
        log.debug("Получен {} запрос {}", request.getMethod(), request.getRequestURI());
        Long ownerId = (long) request.getIntHeader("X-Sharer-User-Id");
        return bookingService.getAllBookingsItemsByUserId(ownerId, state);
    }
}