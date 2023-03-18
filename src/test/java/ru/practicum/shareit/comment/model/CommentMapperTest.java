package ru.practicum.shareit.comment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    CommentMapper commentMapper;

    @Mock
    UserRepository userRepository;

    private Comment comment;
    private  CommentDto commentDto;
    private User user;

    @BeforeEach
    public void add() {
        comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(1L);
        comment.setAuthor(1L);
        comment.setCreated(LocalDateTime.parse("2023-01-02T12:12:12"));

        commentDto = new CommentDto(comment.getId(),
                comment.getText(),
                comment.getItem(),
                "authorName",
                comment.getCreated()
                );

        user = new User();
        user.setId(2L);
        user.setName("authorName");
    }

    @Test
    void toCommentDto() {
        when(userRepository.findUserById(any()))
                .thenReturn(user);

        CommentDto actualCommentDto = commentMapper.toCommentDto(comment);

        assertEquals(commentDto.getId(), actualCommentDto.getId());
        assertEquals(commentDto.getText(), actualCommentDto.getText());
        assertEquals(commentDto.getItem(), actualCommentDto.getItem());
        assertEquals(commentDto.getAuthorName(), actualCommentDto.getAuthorName());
        assertEquals(commentDto.getCreated(), actualCommentDto.getCreated());

    }

    @Test
    void toEntity() {
        Comment actualComment = commentMapper.toEntity(commentDto);

        assertEquals(comment.getText(), actualComment.getText());
        assertEquals(comment.getItem(), actualComment.getItem());
        assertEquals(comment.getCreated(), actualComment.getCreated());
    }

    @Test
    void toEntity_whenCommentDtoIsNull_thenReturnNull() {
        Comment actualComment = commentMapper.toEntity(null);

        assertNull(actualComment);
    }

    @Test
    void mapToCommentDto() {
        when(userRepository.findUserById(any()))
                .thenReturn(user);

        List<CommentDto> commentDtoList = commentMapper.mapToCommentDto(List.of(comment));

        assertEquals(commentDto.getId(), commentDtoList.get(0).getId());
        assertEquals(commentDto.getText(), commentDtoList.get(0).getText());
        assertEquals(commentDto.getItem(), commentDtoList.get(0).getItem());
        assertEquals(commentDto.getAuthorName(), commentDtoList.get(0).getAuthorName());
        assertEquals(commentDto.getCreated(), commentDtoList.get(0).getCreated());
    }
}