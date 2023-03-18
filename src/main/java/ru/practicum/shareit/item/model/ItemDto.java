package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking2ItemDto;
import ru.practicum.shareit.comment.model.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemDto implements Serializable {
    private Long id;
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;
    @NotNull(message = "Отсутствует статус")
    private Boolean available;
    private Long owner;
    private Long requestId;
    private Booking2ItemDto lastBooking;
    private Booking2ItemDto nextBooking;
    private List<CommentDto> comments;
}