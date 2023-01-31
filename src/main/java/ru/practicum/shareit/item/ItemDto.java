package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Builder
@Data
public class ItemDto {
    private final String name;
    private final String description;
    private final boolean available;
    private final Long request;
}