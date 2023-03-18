package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking2ItemDto;
import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.comment.model.CommentMapper;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;

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
    private ItemServiceImpl itemService;

    @MockBean
    private ItemMapper itemMapper;

    @MockBean
    private CommentMapper commentMapper;

    @Test
    @SneakyThrows
    void createItem_whenItemCreated_thenReturnItem() {
        ItemDto itemDto = new ItemDto(null,
                "name",
                "desc",
                true,
                1L,
                1L,
                new Booking2ItemDto(),
                new Booking2ItemDto(),
                new ArrayList<>());
        when(itemService.createItem(any(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
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
        ItemDto itemDto = new ItemDto(null,
                "",
                "desc",
                true,
                1L,
                1L,
                new Booking2ItemDto(),
                new Booking2ItemDto(),
                new ArrayList<>());

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getItemById_whenInvoke_thenReturnOk() {
        Long itemId = 1L;
        Long userId = 1L;
        mockMvc.perform(get("/items/{itemId}", itemId).header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService).getItemById(itemId, userId);
    }

    @Test
    @SneakyThrows
    void getAllItemsByUserId_whenInvoke_thenReturnOk() {
        Long userId = 1L;
        mockMvc.perform(get("/items").header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService).getAllItemsByUserId(userId, 0, 20);
    }

    @Test
    @SneakyThrows
    void searchItem__whenInvoke_thenReturnOk() {
        Long userId = 1L;
        String text = "text";
        mockMvc.perform(get("/items/search").header("X-Sharer-User-Id", userId).param("text", text))
                .andExpect(status().isOk());

        verify(itemService).searchItem(text, 0, 20);
    }

    @Test
    @SneakyThrows
    void updateItem_whenItemUpdated_thenReturnItem() {
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto(null,
                "",
                "desc",
                true,
                1L,
                1L,
                new Booking2ItemDto(),
                new Booking2ItemDto(),
                new ArrayList<>());
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemService).updateItem(anyLong(), any(), anyLong());
    }

    @Test
    @SneakyThrows
    void deleteUser_whenInvoke_thenReturnOk() {
        Long itemId = 1L;
        mockMvc.perform(delete("/items/{itemId}", itemId))
                .andExpect(status().isOk());

        verify(itemService).deleteItem(anyLong());
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
        when(itemService.createComment(any(), anyLong(), anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .content(objectMapper.writeValueAsString(commentDto))
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
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}