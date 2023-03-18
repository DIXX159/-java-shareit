package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemRequestDto implements Serializable {
    private Long id;
    @NotBlank
    private String description;
    private Long requester;
    private LocalDateTime created;
    private List<ItemDto> items;
}