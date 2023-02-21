package ru.practicum.shareit.comment;


import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface CommentService {

    void deleteItem(long userId);
}