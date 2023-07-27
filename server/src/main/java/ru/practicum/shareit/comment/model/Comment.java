package ru.practicum.shareit.comment.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.data.EntityData;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends EntityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @NotBlank(message = "Комментарий не должен быть пустым")
    @Column(name = "text")
    String text;
    @Column(name = "item_id")
    Long item;
    @Column(name = "author_id")
    Long author;
    @Column(name = "created")
    LocalDateTime created;
}