package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, Long bookerId);

    Booking updateApproving(Long bookingId, Long ownerId);


    Booking getBookingById(Long bookingId, Long userId);

    List<Booking> getAllBookingsByUserId(String state, Long userId);

    List<Booking> getAllBookingsItemsByUserId(Long ownerId, String state);
}