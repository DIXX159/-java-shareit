package ru.practicum.shareit.booking.model;

import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDto{
    private Long id;
    @FutureOrPresent(message = "Неверная дата начала бронирования")
    private LocalDateTime start;
    @FutureOrPresent(message = "Неверная дата окончания бронирования")
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private String status;
}