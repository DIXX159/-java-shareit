package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking2Dto {
    Long id;
    @FutureOrPresent(message = "Неверная дата начала бронирования")
    LocalDateTime start;
    @FutureOrPresent(message = "Неверная дата окончания бронирования")
    LocalDateTime end;
    Item item;
    User booker;
    BookingStatus status;
}