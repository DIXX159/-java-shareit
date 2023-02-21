package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.data.EntityData;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "users")
@Getter @Setter
public class User extends EntityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Email(message = "Не верный формат e-mail")
    @Column(name = "email")
    private String email;
}