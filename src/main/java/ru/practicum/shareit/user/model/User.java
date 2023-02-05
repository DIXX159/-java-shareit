package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.data.EntityData;

import javax.validation.constraints.Email;

@Data
public class User extends EntityData {
    private Long id;
    private String name;
    @Email(message = "Не верный формат e-mail")
    private String email;
}