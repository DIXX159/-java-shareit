package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final UserService userService;

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking, Long bookerId) {
        return null;
    }

    @Override
    public Booking updateApproving(Long bookingId, Long ownerId) {
        return null;
    }

    @Override
    public Booking getBookingById(Long bookingId, Long userId) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsByUserId(String state, Long userId) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsItemsByUserId(Long ownerId, String state) {
        return null;
    }
}