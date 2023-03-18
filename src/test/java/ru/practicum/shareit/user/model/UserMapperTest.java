package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void add() {
        user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@mail.ru");

        userDto = new UserDto(user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    @Test
    void toUserDto() {
        UserDto actualUserDto = userMapper.toUserDto(user);

        assertEquals(userDto.getId(), actualUserDto.getId());
        assertEquals(userDto.getName(), actualUserDto.getName());
        assertEquals(userDto.getEmail(), actualUserDto.getEmail());
    }

    @Test
    void toEntity() {
        User actualUser = userMapper.toEntity(userDto);

        assertEquals(userDto.getName(), actualUser.getName());
        assertEquals(userDto.getEmail(), actualUser.getEmail());
    }

    @Test
    void toEntity_whenUserDtoIsNull_thenReturnNull() {
        User actualUser = userMapper.toEntity(null);

        assertNull(actualUser);
    }

    @Test
    void mapToUserDto() {
        List<UserDto> actualUserDtoList = userMapper.mapToUserDto(List.of(user));

        assertEquals(userDto.getId(), actualUserDtoList.get(0).getId());
        assertEquals(userDto.getName(), actualUserDtoList.get(0).getName());
        assertEquals(userDto.getEmail(), actualUserDtoList.get(0).getEmail());
    }
}