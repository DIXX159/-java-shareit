package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ItemDto {
    private Long id;
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;
    @NotNull(message = "Отсутствует статус")
    private Boolean available;
    private Long request;
}