package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking2Dto {
    private Long id;
    @FutureOrPresent(message = "Неверная дата начала бронирования")
    private LocalDateTime start;
    @FutureOrPresent(message = "Неверная дата окончания бронирования")
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;
}