package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking2ItemDto {
    private Long id;
    private String start;
    private String end;
    private Item item;
    private Long bookerId;
    private String status;
}