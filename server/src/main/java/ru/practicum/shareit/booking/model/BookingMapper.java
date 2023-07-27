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
                booking.getStart(),
                booking.getEnd(),
                itemRepository.findItemByIdOrderById(booking.getItem()),
                userRepository.findUserById(booking.getBooker()),
                booking.getStatus()
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
        return booking;
    }

    public List<Booking2Dto> mapToBooking2Dto(Iterable<Booking> bookings) {
        List<Booking2Dto> booking2Dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            booking2Dtos.add(toBookingDto(booking));
        }
        return booking2Dtos;
    }

    public List<Booking2ItemDto> mapToItemBooking2Dto(Iterable<Booking> bookings) {
        List<Booking2ItemDto> booking2ItemDtos = new ArrayList<>();
        for (Booking booking : bookings) {
            booking2ItemDtos.add(toItemBookingDto(booking));
        }
        return booking2ItemDtos;
    }
}