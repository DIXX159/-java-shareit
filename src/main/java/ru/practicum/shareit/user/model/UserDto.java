package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class UserDto {
    private final Long id;
    private final String name;
    @NotNull
    @Email(message = "Не верный формат e-mail")
    private final String email;
}