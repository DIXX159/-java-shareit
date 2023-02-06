package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.data.EntityData;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Data
public class Item extends EntityData {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    @Positive
    private Long owner;
    private ItemRequest request;
}