package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.data.EntityData;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "items")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item extends EntityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @NotBlank(message = "Имя не должно быть пустым")
    @Column(name = "name")
    String name;
    @NotBlank(message = "Описание не должно быть пустым")
    @Column(name = "description")
    String description;
    @NotNull(message = "Отсутствует статус")
    @Column(name = "is_available")
    Boolean available;
    @Positive(message = "Неправильно указан владелец")
    @Column(name = "owner_id")
    Long owner;
    @Column(name = "request_id")
    Long requestId;
}