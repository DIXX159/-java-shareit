package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Builder
@Data
public class UserDto {
    private final Long id;
    private final String name;
    @Email(message = "Не верный формат e-mail")
    private final String email;
}