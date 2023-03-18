package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking2Dto;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface BookingService {
    Booking2Dto createBooking(BookingDto bookingDto, Long bookerId) throws ValidationException;

    Booking2Dto updateApproving(Long bookingId, Long ownerId, String approved) throws ValidationException;

    Booking2Dto getBookingById(Long bookingId, Long userId) throws ValidationException;

    List<Booking2Dto> getAllBookingsByUserId(String state, Long userId, PageRequest pageRequest) throws ThrowableException;

    List<Booking2Dto> getAllBookingsItemsByUserId(Long ownerId, String state, PageRequest pageRequest) throws ThrowableException;
}