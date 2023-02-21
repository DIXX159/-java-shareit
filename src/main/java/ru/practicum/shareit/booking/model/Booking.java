package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.data.EntityData;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter @Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @Column(name = "item_id")
    private Long item;
    @Column(name = "booker_id")
    private Long booker;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}