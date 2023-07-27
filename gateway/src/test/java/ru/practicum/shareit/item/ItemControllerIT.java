package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking2ItemDto;
import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto(1L,
                "name",
                "desc",
                true,
                1L,
                1L,
                new Booking2ItemDto(),
                new Booking2ItemDto(),
                new ArrayList<>()
        );
    }

    @Test
    @SneakyThrows
    void createItem_whenItemCreated_thenReturnItem() {
        when(itemClient.createItem(any(), anyLong()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void createItem_whenItemIsNotValid_thenReturnBadRequest() {
        itemDto.setName("");

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getItemById_whenInvoke_thenReturnOk() {
        long itemId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/items/{itemId}", itemId).header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemClient).getItemById(itemId, userId);
    }

    @Test
    @SneakyThrows
    void getAllItemsByUserId_whenInvoke_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(get("/items").header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemClient).getAllItemsByUserId(userId, 0, 20);
    }

    @Test
    @SneakyThrows
    void searchItem__whenInvoke_thenReturnOk() {
        long userId = 1L;
        String text = "text";
        mockMvc.perform(get("/items/search").header("X-Sharer-User-Id", userId).param("text", text))
                .andExpect(status().isOk());

        verify(itemClient).searchItem(text, userId, 0, 20);
    }

    @Test
    @SneakyThrows
    void updateItem_whenItemUpdated_thenReturnItem() {
        Long itemId = 1L;
        when(itemClient.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(ResponseEntity.ok(itemDto));

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemClient).updateItem(anyLong(), any(), anyLong());
    }

    @Test
    @SneakyThrows
    void deleteUser_whenInvoke_thenReturnOk() {
        Long itemId = 1L;
        mockMvc.perform(delete("/items/{itemId}", itemId))
                .andExpect(status().isOk());

        verify(itemClient).deleteItem(anyLong());
    }

    @Test
    @SneakyThrows
    void createComment_whenCommentCreated_thenReturnComment() {
        Long itemId = 1L;
        CommentDto commentDto = new CommentDto(null,
                "text",
                1L,
                "name",
                LocalDateTime.now()
        );
        when(itemClient.createComment(any(), anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(commentDto));

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

    @Test
    @SneakyThrows
    void createComment_whenCommentIsNotValid_thenReturnBadRequest() {
        Long itemId = 1L;
        CommentDto commentDto = new CommentDto(null,
                "",
                1L,
                "name",
                LocalDateTime.now()
        );

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}