package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.BookingDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto(
                null,
                LocalDateTime.parse("2023-03-26T21:27:43"),
                LocalDateTime.parse("2023-03-26T21:27:43").plusMinutes(1),
                1L,
                1L,
                BookingStatus.REJECTED.toString()
        );
    }

    @Test
    @SneakyThrows
    void createBooking_whenBookingCreated_thenReturnBooking() {
        when(bookingClient.createBooking(any(), anyLong()))
                .thenReturn(ResponseEntity.ok(bookingDto));
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())));
    }

    @Test
    @SneakyThrows
    void createBooking_whenBookingIsNotValid_thenReturnBadRequest() {
        BookingDto bookingDto = new BookingDto(null,
                LocalDateTime.now().minusHours(10),
                LocalDateTime.now().plusMinutes(15),
                1L,
                1L,
                "APPROVED"
        );

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void bookingApproved() {
        Long bookingId = 1L;
        String text = "true";

        when(bookingClient.updateApproving(anyLong(), anyLong(), anyString()))
                .thenReturn(ResponseEntity.ok(bookingDto));

        mockMvc.perform(patch("/bookings//{bookingId}", bookingId).param("approved", text)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())));

        verify(bookingClient).updateApproving(anyLong(), anyLong(), anyString());
    }

    @Test
    @SneakyThrows
    void getBookingById_whenInvoke_thenReturnOk() {
        long bookingId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", bookingId).header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingClient).getBookingById(bookingId, userId);
    }

    @Test
    @SneakyThrows
    void getBookingItemsByUser_whenInvoke_thenReturnOk() {
        long userId = 1L;
        String stateParam = "ALL";
        mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", userId).param("state", stateParam))
                .andExpect(status().isOk());
        BookingStatus state = BookingStatus.from(stateParam).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        verify(bookingClient).getAllBookingsItemsByUserId(state, userId, 0, 20);
    }

    @Test
    @SneakyThrows
    void getBookingByUser_whenInvoke_thenReturnOk() {
        long userId = 1L;
        String stateParam = "ALL";
        mockMvc.perform(get("/bookings").param("state", stateParam).header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
        BookingStatus state = BookingStatus.from(stateParam).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        verify(bookingClient).getAllBookingsByUserId(state, userId, 0, 20);
    }
}