package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
