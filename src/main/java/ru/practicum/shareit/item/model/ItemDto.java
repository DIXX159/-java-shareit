package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    @NotBlank(message = "Имя не должно быть пустым")
    private final String name;
    @NotBlank(message = "Описание не должно быть пустым")
    private final String description;
    @NotNull(message = "Отсутствует статус")
    private final Boolean available;
    private final Long request;
}