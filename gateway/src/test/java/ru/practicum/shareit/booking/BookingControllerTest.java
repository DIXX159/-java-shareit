package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    BookingClient bookingClient;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto(
                null,
                LocalDateTime.parse("2023-03-11T21:27:43"),
                LocalDateTime.parse("2023-03-11T21:27:43").plusMinutes(1),
                1L,
                1L,
                BookingStatus.REJECTED.toString()
        );
    }

    @Test
    @SneakyThrows
    void createBooking() {
        when(bookingClient.createBooking(bookingDto, 1L))
                .thenReturn(ResponseEntity.accepted().body(bookingDto));

        ResponseEntity<Object> response = bookingClient.createBooking(bookingDto, 1L);

        assertEquals(response.getBody(), bookingDto);
    }

    @Test
    @SneakyThrows
    void bookingApproved() {
        bookingDto.setStatus(BookingStatus.APPROVED.toString());
        when(bookingClient.updateApproving(1L, 1L, "approved"))
                .thenReturn(ResponseEntity.accepted().body(bookingDto));

        ResponseEntity<Object> response = bookingClient.updateApproving(1L, 1L, "approved");

        assertEquals(response.getBody(), bookingDto);
    }

    @Test
    @SneakyThrows
    void getBookingById() {
        when(bookingClient.getBookingById(1L, 1L))
                .thenReturn(ResponseEntity.accepted().body(List.of(bookingDto)));

        ResponseEntity<Object> response = bookingClient.getBookingById(1L, 1L);

        assertEquals(response.getBody(), List.of(bookingDto));
    }

    @Test
    @SneakyThrows
    void getBookingByUser() {
        when(bookingClient.getAllBookingsByUserId(BookingStatus.REJECTED, 1L, 0, 1))
                .thenReturn(ResponseEntity.accepted().body(List.of(bookingDto)));

        ResponseEntity<Object> response = bookingClient.getAllBookingsByUserId(BookingStatus.REJECTED, 1L, 0, 1);

        assertEquals(response.getBody(), List.of(bookingDto));
    }

    @Test
    @SneakyThrows
    void getBookingItemsByUser() {
        when(bookingClient.getAllBookingsItemsByUserId(BookingStatus.REJECTED, 1L, 0, 1))
                .thenReturn(ResponseEntity.accepted().body(List.of(bookingDto)));

        ResponseEntity<Object> response = bookingClient.getAllBookingsItemsByUserId(BookingStatus.REJECTED, 1L, 0, 1);

        assertEquals(response.getBody(), List.of(bookingDto));
    }
}