package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CommentDto {
    private String text;
    private Long item;
    private Long author;
    private LocalDate created;
}