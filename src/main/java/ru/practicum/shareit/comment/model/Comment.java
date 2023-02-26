package ru.practicum.shareit.comment.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.data.EntityData;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment extends EntityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotBlank
    @Column(name = "text")
    private String text;
    @Column(name = "item_id")
    private Long item;
    @Column(name = "author_id")
    private Long author;
    @Column(name = "created")
    private LocalDateTime created;
}