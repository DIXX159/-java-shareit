package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking2Dto;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    BookingService bookingService;

    private final Booking booking = new Booking();
    private Booking2Dto booking2Dto;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        booking.setStart(LocalDateTime.parse("2023-03-11T21:27:43"));

        booking2Dto = new Booking2Dto(
                null,
                LocalDateTime.parse("2023-03-11T21:27:43"),
                LocalDateTime.parse("2023-03-11T21:27:43").plusMinutes(1),
                new Item(),
                new User(),
                BookingStatus.APPROVED
        );
        bookingDto = new BookingDto(
                null,
                LocalDateTime.parse("2023-03-11T21:27:43"),
                LocalDateTime.parse("2023-03-11T21:27:43").plusMinutes(1),
                1L,
                1L,
                "APPROVED"
        );
    }

    @Test
    @SneakyThrows
    void createBooking() {
        when(bookingService.createBooking(bookingDto, 1L))
                .thenReturn(booking2Dto);

        Booking2Dto response = bookingService.createBooking(bookingDto, 1L);

        assertEquals(response.getStart(), booking.getStart());
    }

    @Test
    @SneakyThrows
    void bookingApproved() {
        booking2Dto.setStatus(BookingStatus.APPROVED);
        when(bookingService.updateApproving(1L, 1L, "approved"))
                .thenReturn(booking2Dto);

        Booking2Dto response = bookingService.updateApproving(1L, 1L, "approved");

        assertEquals(response.getStatus(), BookingStatus.APPROVED);

    }

    @Test
    @SneakyThrows
    void getBookingById() {
        when(bookingService.getBookingById(1L, 1L))
                .thenReturn(booking2Dto);

        Booking2Dto response = bookingService.getBookingById(1L, 1L);

        assertEquals(response.getStart(), booking2Dto.getStart());
    }

    @Test
    @SneakyThrows
    void getBookingByUser() {
        when(bookingService.getAllBookingsByUserId("State", 1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking2Dto));

        List<Booking2Dto> response = bookingService.getAllBookingsByUserId("State", 1L, PageRequest.of(0, 1));

        assertEquals(response.get(0).getStart(), booking2Dto.getStart());
    }

    @Test
    @SneakyThrows
    void getBookingItemsByUser() {
        when(bookingService.getAllBookingsItemsByUserId(1L, "State", PageRequest.of(0, 1)))
                .thenReturn(List.of(booking2Dto));

        List<Booking2Dto> response = bookingService.getAllBookingsItemsByUserId(1L, "State", PageRequest.of(0, 1));

        assertEquals(response.get(0).getStart(), booking2Dto.getStart());
    }
}