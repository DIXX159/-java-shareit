package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;
    @FutureOrPresent(message = "Неверная дата начала бронирования")
    LocalDateTime start;
    @FutureOrPresent(message = "Неверная дата окончания бронирования")
    LocalDateTime end;
    Long itemId;
    Long bookerId;
    String status;
}