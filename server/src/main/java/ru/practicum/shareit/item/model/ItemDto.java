package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking2ItemDto;
import ru.practicum.shareit.comment.model.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    @NotBlank(message = "Имя не должно быть пустым")
    String name;
    @NotBlank(message = "Описание не должно быть пустым")
    String description;
    @NotNull(message = "Отсутствует статус")
    Boolean available;
    Long owner;
    Long requestId;
    Booking2ItemDto lastBooking;
    Booking2ItemDto nextBooking;
    List<CommentDto> comments;
}