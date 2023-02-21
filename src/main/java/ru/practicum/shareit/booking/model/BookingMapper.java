package ru.practicum.shareit.booking.model;

import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public Booking toEntity(@Valid BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(bookingDto.getItemId());
        booking.setBooker(bookingDto.getBookerId());
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }
}