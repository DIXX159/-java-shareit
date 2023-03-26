package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    Long id;
    @NotBlank(message = "Комментарий не должен быть пустым")
    private String text;
    private Long item;
    private String authorName;
    private LocalDateTime created;
}