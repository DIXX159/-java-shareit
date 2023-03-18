package ru.practicum.shareit.user.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> userList = new ArrayList<>();
        for (User user : users) {
            userList.add(toUserDto(user));
        }
        return userList;
    }
}