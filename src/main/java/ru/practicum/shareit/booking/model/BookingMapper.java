package ru.practicum.shareit.booking.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingMapper(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Booking2Dto toBookingDto(Booking booking) {
        return new Booking2Dto(
                booking.getId(),
                booking.getStart().toString(),
                booking.getEnd().toString(),
                itemRepository.findItemByIdOrderById(booking.getItem()),
                userRepository.findUserById(booking.getBooker()),
                booking.getStatus().toString()
        );
    }

    public Booking2ItemDto toItemBookingDto(Booking booking) {
        return new Booking2ItemDto(
                booking.getId(),
                booking.getStart().toString(),
                booking.getEnd().toString(),
                itemRepository.findItemByIdOrderById(booking.getItem()),
                userRepository.findUserById(booking.getBooker()).getId(),
                booking.getStatus().toString()
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

    public List<Booking2Dto> mapToBooking2Dto(Iterable<Booking> bookings) {
        List<Booking2Dto> Booking2Dto = new ArrayList<>();
        for (Booking booking : bookings) {
            Booking2Dto.add(toBookingDto(booking));
        }
        return Booking2Dto;
    }

    public List<Booking2ItemDto> mapToItemBooking2Dto(Iterable<Booking> bookings) {
        List<Booking2ItemDto> Booking2ItemDto = new ArrayList<>();
        for (Booking booking : bookings) {
            Booking2ItemDto.add(toItemBookingDto(booking));
        }
        return Booking2ItemDto;
    }
}