package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.BookingStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter @Setter
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotBlank
    @Column(name = "description")
    private String description;
    @Column(name = "requestor_id")
    private Long requestorId;
}