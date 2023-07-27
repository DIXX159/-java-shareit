package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto(1L,
                "name1",
                "name1desc",
                true,
                1L,
                null,
                null,
                null,
                new ArrayList<>()
        );
    }

    @Test
    @SneakyThrows
    void createItem() {
        when(itemClient.createItem(itemDto, 1L))
                .thenReturn(ResponseEntity.accepted().body(itemDto));

        ResponseEntity<Object> response = itemClient.createItem(itemDto, 1L);

        assertEquals(itemDto, response.getBody());
    }

    @Test
    @SneakyThrows
    void getItemById() {
        when(itemClient.getItemById(itemDto.getId(), 1L))
                .thenReturn(ResponseEntity.accepted().body(itemDto));
        ResponseEntity<Object> response = itemClient.getItemById(itemDto.getId(), 1L);

        assertEquals(itemDto, response.getBody());
    }

    @Test
    @SneakyThrows
    void getAllItemsByUserId() {
        when(itemClient.getAllItemsByUserId(1L, 0, 1))
                .thenReturn(ResponseEntity.accepted().body(List.of(itemDto)));
        ResponseEntity<Object> response = itemClient.getAllItemsByUserId(1L, 0, 1);

        assertEquals(List.of(itemDto), response.getBody());
    }

    @Test
    @SneakyThrows
    void searchItem() {
        when(itemClient.searchItem("text", 1L, 0, 1))
                .thenReturn(ResponseEntity.accepted().body(List.of(itemDto)));
        ResponseEntity<Object> response = itemClient.searchItem("text", 1L, 0, 1);

        assertEquals(List.of(itemDto), response.getBody());
    }

    @Test
    @SneakyThrows
    void updateItem() {
        when(itemClient.updateItem(1L, itemDto, 1L))
                .thenReturn(ResponseEntity.accepted().body(itemDto));
        ResponseEntity<Object> response = itemClient.updateItem(1L, itemDto, 1L);

        assertEquals(itemDto, response.getBody());
    }

    @Test
    @SneakyThrows
    void createComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Comment");

        when(itemClient.createComment(commentDto, 1L, 1L))
                .thenReturn(ResponseEntity.accepted().body(commentDto));
        ResponseEntity<Object> response = itemClient.createComment(commentDto, 1L, 1L);

        assertEquals(commentDto, response.getBody());
    }
}