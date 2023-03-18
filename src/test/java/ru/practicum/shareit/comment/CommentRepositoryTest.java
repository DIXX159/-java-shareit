package ru.practicum.shareit.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    Comment comment;

    @BeforeEach
    public void addBookings() {
        commentRepository.deleteAll();
        comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(1L);
        comment.setAuthor(1L);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Test
    void findByItemAndAuthor() {
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("comment2");
        comment2.setItem(1L);
        comment2.setAuthor(2L);
        comment2.setCreated(LocalDateTime.now());
        commentRepository.save(comment2);

        Comment findComment = commentRepository.findByItemAndAuthor(1L, 1L);

        assertEquals(commentRepository.findAll().size(), 2);
        assertEquals(comment.getText(), findComment.getText());
    }

    @Test
    void findCommentsByItem() {
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("comment2");
        comment2.setItem(1L);
        comment2.setAuthor(2L);
        comment2.setCreated(LocalDateTime.now());
        commentRepository.save(comment2);

        List<Comment> commentsByItem = commentRepository.findCommentsByItem(1L);

        assertEquals(commentRepository.findAll().size(), 2);
        assertEquals(commentsByItem.size(), 2);
        assertEquals(comment2.getText(), commentsByItem.get(1).getText());
    }
}