package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {
    private Long id;
    private String name;
    @Email(message = "Не верный формат e-mail")
    private String email;
}