package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking2Dto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
    private BookingService bookingService;

    @Test
    @SneakyThrows
    void createBooking_whenBookingCreated_thenReturnBooking() {
        Booking2Dto booking2Dto = new Booking2Dto(null,
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusMinutes(15),
                new Item(),
                new User(),
                BookingStatus.APPROVED
        );
        when(bookingService.createBooking(any(), anyLong()))
                .thenReturn(booking2Dto);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(booking2Dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(booking2Dto.getStatus().toString())));
    }

    @Test
    @SneakyThrows
    void createBooking_whenBookingIsNotValid_thenReturnBadRequest() {
        Booking2Dto booking2Dto = new Booking2Dto(null,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                new Item(),
                new User(),
                BookingStatus.APPROVED
        );

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(booking2Dto))
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
        Booking2Dto booking2Dto = new Booking2Dto(null,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                new Item(),
                new User(),
                BookingStatus.APPROVED
        );
        when(bookingService.updateApproving(anyLong(), anyLong(), anyString()))
                .thenReturn(booking2Dto);

        mockMvc.perform(patch("/bookings//{bookingId}", bookingId).param("approved", text)
                        .content(objectMapper.writeValueAsString(booking2Dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(booking2Dto.getStatus().toString())));

        verify(bookingService).updateApproving(anyLong(), anyLong(), anyString());
    }

    @Test
    @SneakyThrows
    void getBookingById_whenInvoke_thenReturnOk() {
        Long bookingId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", bookingId).header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingService).getBookingById(bookingId, userId);
    }

    @Test
    @SneakyThrows
    void getBookingItemsByUser_whenInvoke_thenReturnOk() {
        Long userId = 1L;
        String state = "ALL";
        mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", userId).param("state", state))
                .andExpect(status().isOk());

        verify(bookingService).getAllBookingsItemsByUserId(userId, state, PageRequest.of(0, 20));
    }

    @Test
    @SneakyThrows
    void getBookingByUser_whenInvoke_thenReturnOk() {
        Long userId = 1L;
        String state = "ALL";
        mockMvc.perform(get("/bookings").param("state", state).header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingService).getAllBookingsByUserId(state, userId, PageRequest.of(0, 20));
    }
}