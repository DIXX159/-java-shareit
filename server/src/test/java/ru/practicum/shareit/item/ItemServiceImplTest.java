package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.comment.model.CommentMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    ItemServiceImpl itemService;

    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemMapper itemMapper;
    @Mock
    BookingMapper bookingMapper;
    @Mock
    CommentMapper commentMapper;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    ItemDto itemDto;
    Item item;

    @BeforeEach
    public void addItems() {
        itemDto = new ItemDto(
                1L,
                "name",
                "desc",
                true,
                1L,
                1L,
                null,
                null,
                new ArrayList<>()
        );
        item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(1L);
        item.setRequestId(1L);
    }

    @Test
    @SneakyThrows
    void createItem_whenItemCreated_thenItemSave() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findUserById(1L))
                .thenReturn(user);
        when(itemMapper.toEntity(itemDto))
                .thenReturn(item);
        when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);
        when(itemRepository.save(item))
                .thenReturn(item);

        ItemDto actualItem = itemService.createItem(itemDto, 1L);

        assertEquals(itemDto, actualItem);
        verify(itemRepository).save(item);
    }

    @Test
    @SneakyThrows
    void createItem_whenAvailableIsNull_thenValidationExceptionThrown() {
        item.setAvailable(null);
        when(itemMapper.toEntity(itemDto))
                .thenReturn(item);

        assertThrows(ValidationException.class, () -> itemService.createItem(itemDto, 1L));
        verify(itemRepository, never()).save(item);
    }

    @Test
    @SneakyThrows
    void createItem_whenUserIdIs0_thenThrowableExceptionThrown() {
        assertThrows(ThrowableException.class, () -> itemService.createItem(itemDto, 0L));
        verify(itemRepository, never()).save(item);
    }

    @Test
    @SneakyThrows
    void createItem_whenUserIsNotFound_thenNotFoundExceptionThrown() {
        when(itemMapper.toEntity(itemDto))
                .thenReturn(item);
        assertThrows(NotFoundException.class, () -> itemService.createItem(itemDto, 1L));
        verify(itemRepository, never()).save(item);
    }

    @Test
    @SneakyThrows
    void createItem_whenItemIsExist_thenThrowableExceptionThrown() {
        Item item = new Item();
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        ItemDto itemDto = itemMapper.toItemDto(item);
        when(itemMapper.toEntity(itemDto))
                .thenReturn(item);
        when(userRepository.findUserById(1L))
                .thenReturn(new User());
        when(itemRepository.findItemByNameAndDescription("name", "desc"))
                .thenReturn(new Item());

        assertThrows(ThrowableException.class, () -> itemService.createItem(itemDto, 1L));
        verify(itemRepository, never()).save(item);
    }

    @Test
    @SneakyThrows
    void updateItem_whenItemUpdated_thenItemSave() {
        Item item = new Item();
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(1L);
        ItemDto itemDto = itemMapper.toItemDto(item);
        when(itemMapper.toEntity(itemDto))
                .thenReturn(item);
        when(itemRepository.findItemByIdOrderById(1L))
                .thenReturn(item);

        itemService.updateItem(1L, itemDto, 1L);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals("name", savedItem.getName());
        assertEquals("desc", savedItem.getDescription());
    }

    @Test
    @SneakyThrows
    void updateItem_whenUserIsNotOwner_thenNotFoundExceptionThrown() {
        when(itemMapper.toEntity(itemDto))
                .thenReturn(item);
        when(itemRepository.findItemByIdOrderById(1L))
                .thenReturn(item);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(1L, itemDto, 2L));
        verify(itemRepository, never()).save(item);
    }

    @Test
    @SneakyThrows
    void updateItem_whenUserIdIs0_thenThrowableExceptionThrown() {
        Item item = new Item();
        ItemDto itemDto = itemMapper.toItemDto(item);
        when(itemMapper.toEntity(itemDto))
                .thenReturn(item);

        assertThrows(ThrowableException.class, () -> itemService.updateItem(1L, itemDto, 0L));
        verify(itemRepository, never()).save(item);
    }

    @Test
    @SneakyThrows
    void updateItem_whenItemNotFound_thenNotFoundExceptionThrown() {
        Item item = new Item();
        ItemDto itemDto = itemMapper.toItemDto(item);
        when(itemMapper.toEntity(itemDto))
                .thenReturn(item);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(1L, itemDto, 1L));
        verify(itemRepository, never()).save(item);
    }

    @Test
    void getAllItemsByUserId_whenItemsFound_thenItemsGet() {
        Item item = new Item();

        List<ItemDto> items = List.of(itemDto);
        when(itemRepository.findAllByOwnerOrderById(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(item));
        when(itemMapper.mapToItemDto(List.of(item)))
                .thenReturn(items);
        when(bookingMapper.mapToItemBooking2Dto(any()))
                .thenReturn(new ArrayList<>());
        when(commentMapper.mapToCommentDto(any()))
                .thenReturn(new ArrayList<>());

        List<ItemDto> response = itemService.getAllItemsByUserId(1L, 0, 1);

        assertEquals(items, response);
    }

    @Test
    void getAllItemsByUserId_whenItemsNotFound_thenNotFoundExceptionThrown() {
        when(itemRepository.findAllByOwnerOrderById(1L, PageRequest.of(0, 1)))
                .thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> itemService.getAllItemsByUserId(1L, 0, 1));
    }

    @Test
    void getAllItems_whenItemsFound_thenItemsGet() {
        List<ItemDto> items = List.of(itemDto);
        when(itemMapper.mapToItemDto(any()))
                .thenReturn(items);
        when(bookingMapper.mapToItemBooking2Dto(any()))
                .thenReturn(new ArrayList<>());
        when(commentMapper.mapToCommentDto(any()))
                .thenReturn(new ArrayList<>());

        List<ItemDto> response = itemService.getAllItems();

        assertEquals(items, response);
    }

    @Test
    void getItemById_whenItemFound_thenItemGet() {
        Item item = new Item();

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(bookingMapper.mapToItemBooking2Dto(any()))
                .thenReturn(new ArrayList<>());
        when(itemMapper.toItemDto(any()))
                .thenReturn(itemDto);
        when(commentMapper.mapToCommentDto(any()))
                .thenReturn(new ArrayList<>());

        ItemDto response = itemService.getItemById(1L, 1L);

        assertEquals(itemDto, response);
    }

    @Test
    void getItemById_whenItemNotFound_thenNotFoundExceptionThrown() {
        when(itemRepository.findById(1L))
                .thenThrow(new NotFoundException(Constants.itemNotFound));

        assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }

    @Test
    void searchItem_whenItemsFound_thenReturnItems() {
        when(itemMapper.mapToItemDto(any()))
                .thenReturn(List.of(itemDto));

        List<ItemDto> response = itemService.searchItem("desc", 0, 1);

        assertEquals(List.of(itemDto), response);
    }

    @Test
    void searchItem_whenTextIsEmpty_thenReturnEmptyList() {

        List<ItemDto> response = itemService.searchItem("", 0, 1);

        assertEquals(new ArrayList<>(), response);
    }

    @Test
    void searchItem_whenItemsNotFound_thenReturnEmptyList() {
        when(itemMapper.mapToItemDto(any()))
                .thenReturn(new ArrayList<>());

        List<ItemDto> response = itemService.searchItem("text", 0, 1);

        assertEquals(new ArrayList<>(), response);
    }

    @Test
    void deleteItem_whenItemNotFound_thenNotFoundExceptionThrown() {
        when(itemRepository.findItemByIdOrderById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> itemService.deleteItem(1L));
        verify(itemRepository, never()).deleteById(1L);
    }

    @Test
    @SneakyThrows
    void createComment_whenCommentCreated_thenCommentSaved() {
        Item item = new Item();
        Comment comment = new Comment();
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.parse("2023-03-15T19:58"));
        booking.setEnd(LocalDateTime.parse("2023-03-15T19:58"));
        booking.setBooker(1L);
        booking.setItem(1L);
        booking.setStatus(BookingStatus.APPROVED);
        List<Booking> bookings = List.of(booking);

        when(commentMapper.toEntity(commentMapper.toCommentDto(comment)))
                .thenReturn(comment);
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findBookingsByBookerAndItemAndStartBeforeAndEndBefore(1L, 1L, LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0)))
                .thenReturn(Optional.of(bookings));

        CommentDto response = itemService.createComment(commentMapper.toCommentDto(comment), 1L, 1L);

        assertEquals(commentMapper.toCommentDto(comment), response);
        verify(commentRepository).save(comment);
    }

    @Test
    @SneakyThrows
    void createComment_whenBookingNotEnd_thenValidationExceptionThrown() {
        Item item = new Item();
        Comment comment = new Comment();

        when(commentMapper.toEntity(commentMapper.toCommentDto(comment)))
                .thenReturn(comment);
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findBookingsByBookerAndItemAndStartBeforeAndEndBefore(1L, 1L, LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0)))
                .thenReturn(Optional.of(new ArrayList<>()));

        assertThrows(ValidationException.class, () -> itemService.createComment(commentMapper.toCommentDto(comment), 1L, 1L));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    @SneakyThrows
    void createComment_whenItemNotFound_thenNotFoundExceptionThrown() {
        Comment comment = new Comment();
        when(commentMapper.toEntity(commentMapper.toCommentDto(comment)))
                .thenReturn(comment);

        assertThrows(NotFoundException.class, () -> itemService.createComment(commentMapper.toCommentDto(comment), 1L, 1L));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    @SneakyThrows
    void createComment_whenUserNotFound_thenNotFoundExceptionThrown() {
        Item item = new Item();
        Comment comment = new Comment();
        when(commentMapper.toEntity(commentMapper.toCommentDto(comment)))
                .thenReturn(comment);

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> itemService.createComment(commentMapper.toCommentDto(comment), 1L, 1L));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    @SneakyThrows
    void createComment_whenBookingNotFound_thenValidationExceptionThrown() {
        Item item = new Item();
        Comment comment = new Comment();
        when(commentMapper.toEntity(commentMapper.toCommentDto(comment)))
                .thenReturn(comment);

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));

        assertThrows(ValidationException.class, () -> itemService.createComment(commentMapper.toCommentDto(comment), 1L, 1L));
        verify(commentRepository, never()).save(comment);
    }
}