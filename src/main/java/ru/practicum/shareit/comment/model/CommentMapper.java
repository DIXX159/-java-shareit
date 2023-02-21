package ru.practicum.shareit.comment.model;

import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                comment.getCreated()
        );
    }

    public Comment toEntity(@Valid CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(commentDto.getItem());
        comment.setAuthor(commentDto.getAuthor());
        comment.setCreated(commentDto.getCreated());
        return comment;
    }
}