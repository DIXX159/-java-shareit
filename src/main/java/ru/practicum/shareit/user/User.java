package ru.practicum.shareit.user;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class User extends UserData<Long> {
    private Long id;
    private String name;
    @NotNull
    @Email(message = "Не верный формат e-mail")
    private String email;
}