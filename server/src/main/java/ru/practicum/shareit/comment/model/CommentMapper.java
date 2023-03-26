package ru.practicum.shareit.comment.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    private final UserRepository userRepository;

    public CommentMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                userRepository.findUserById(comment.getAuthor()).getName(),
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
        comment.setCreated(commentDto.getCreated());
        return comment;
    }

    public List<CommentDto> mapToCommentDto(Iterable<Comment> comments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtoList.add(toCommentDto(comment));
        }
        if (!commentDtoList.isEmpty()) {
            return commentDtoList;
        } else return null;
    }
}