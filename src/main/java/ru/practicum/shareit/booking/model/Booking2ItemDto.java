package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking2ItemDto implements Serializable {
    private Long id;
    private String start;
    private String end;
    private Item item;
    private Long bookerId;
    private String status;
}