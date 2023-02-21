package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
}