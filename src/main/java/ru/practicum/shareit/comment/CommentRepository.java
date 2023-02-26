package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByItemAndAuthor(Long itemId, Long userId);

    List<Comment> findCommentsByItem(Long itemId);
}