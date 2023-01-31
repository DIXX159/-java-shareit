package ru.practicum.shareit.user;

import lombok.Builder;

@Builder
public class UserDto {
    private final String name;
    private final String email;
}