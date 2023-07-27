package ru.practicum.shareit.comment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    @NotBlank(message = "Комментарий не должен быть пустым")
    String text;
    Long item;
    String authorName;
    LocalDateTime created;
}