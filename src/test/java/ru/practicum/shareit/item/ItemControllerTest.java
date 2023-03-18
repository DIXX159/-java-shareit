package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;
    @Mock
    ItemMapper itemMapper;



    private final Item item1 = new Item();
    private ItemDto itemDto;


    @BeforeEach
    void setUp() {
        item1.setName("name1");
        itemDto = new ItemDto(null,
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
        when(itemService.createItem(item1, 1L))
                .thenReturn(itemDto);

        ItemDto response = itemService.createItem(item1, 1L);

        assertEquals(response.getName(), item1.getName());
    }

    @Test
    @SneakyThrows
    void getItemById() {
        when(itemService.getItemById(itemDto.getId(), 1L))
                .thenReturn(itemDto);
        ItemDto response = itemService.getItemById(itemDto.getId(), 1L);

        assertEquals(itemDto, response);
    }

    @Test
    @SneakyThrows
    void getAllItemsByUserId() {
        when(itemService.getAllItemsByUserId(1L, 0, 1))
                .thenReturn(List.of(itemDto));
        List<ItemDto> response = itemService.getAllItemsByUserId(1L, 0, 1);

        assertEquals(itemDto, response.get(0));
    }

    @Test
    @SneakyThrows
    void searchItem() {
        when(itemService.searchItem("text", 0, 1))
                .thenReturn(List.of(itemDto));
        List<ItemDto> response = itemService.searchItem("text", 0, 1);

        assertEquals(itemDto, response.get(0));
    }

    @Test
    @SneakyThrows
    void updateItem() {
        ItemDto expectedItem = itemMapper.toItemDto(item1);

        when(itemService.updateItem(1L, item1, 1L))
                .thenReturn(expectedItem);
        ItemDto response = itemService.updateItem(1L, item1, 1L);

        assertEquals(expectedItem, response);
    }

    @Test
    @SneakyThrows
    void createComment() {
        Comment comment = new Comment();
        comment.setText("Comment");
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Comment");

        when(itemService.createComment(comment, 1L, 1L))
                .thenReturn(commentDto);
        CommentDto response = itemService.createComment(comment, 1L, 1L);

        assertEquals(comment.getText(), response.getText());
    }
}