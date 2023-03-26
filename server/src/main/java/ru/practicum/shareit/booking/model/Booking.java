package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.data.EntityData;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking extends EntityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "start_date")
    LocalDateTime start;
    @Column(name = "end_date")
    LocalDateTime end;
    @Column(name = "item_id")
    Long item;
    @Column(name = "booker_id")
    Long booker;
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}