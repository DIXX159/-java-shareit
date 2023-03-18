package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @InjectMocks
    BookingMapper bookingMapper;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;

    private Booking booking;
    private BookingDto bookingDto;
    private Booking2Dto booking2Dto;
    private Booking2ItemDto booking2ItemDto;
    private Item item;
    private User user;

    @BeforeEach
    public void add() {
        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.parse("2023-01-01T12:12:12"));
        booking.setEnd(LocalDateTime.parse("2023-01-02T12:12:12"));
        booking.setItem(4L);
        booking.setBooker(2L);
        booking.setStatus(BookingStatus.APPROVED);

        bookingDto = new BookingDto(
                1L,
                LocalDateTime.parse("2023-01-01T12:12:12"),
                LocalDateTime.parse("2023-01-02T12:12:12"),
                4L,
                2L,
                "APPROVED"
        );

        item = new Item();
        item.setId(4L);
        user = new User();
        user.setId(2L);
        booking2Dto = new Booking2Dto(1L,
                LocalDateTime.parse("2023-01-01T12:12:12"),
                LocalDateTime.parse("2023-01-02T12:12:12"),
                item,
                user,
                BookingStatus.APPROVED
        );

        booking2ItemDto = new Booking2ItemDto(1L,
                "2023-01-01T12:12:12",
                "2023-01-02T12:12:12",
                item,
                user.getId(),
                "APPROVED"
        );
    }

    @Test
    void toBookingDto() {
        when(itemRepository.findItemByIdOrderById(any()))
                .thenReturn(item);
        when(userRepository.findUserById(any()))
                .thenReturn(user);

        Booking2Dto actualBooking2Dto = bookingMapper.toBookingDto(booking);

        assertEquals(booking2Dto.getId(), actualBooking2Dto.getId());
        assertEquals(booking2Dto.getStart(), actualBooking2Dto.getStart());
        assertEquals(booking2Dto.getEnd(), actualBooking2Dto.getEnd());
        assertEquals(booking2Dto.getItem(), actualBooking2Dto.getItem());
        assertEquals(booking2Dto.getBooker(), actualBooking2Dto.getBooker());
        assertEquals(booking2Dto.getStatus(), actualBooking2Dto.getStatus());
    }

    @Test
    void toItemBookingDto() {
        when(itemRepository.findItemByIdOrderById(any()))
                .thenReturn(item);
        when(userRepository.findUserById(any()))
                .thenReturn(user);

        Booking2ItemDto actualBooking2ItemDto = bookingMapper.toItemBookingDto(booking);

        assertEquals(booking2ItemDto.getId(), actualBooking2ItemDto.getId());
        assertEquals(booking2ItemDto.getStart(), actualBooking2ItemDto.getStart());
        assertEquals(booking2ItemDto.getEnd(), actualBooking2ItemDto.getEnd());
        assertEquals(booking2ItemDto.getItem(), actualBooking2ItemDto.getItem());
        assertEquals(booking2ItemDto.getBookerId(), actualBooking2ItemDto.getBookerId());
        assertEquals(booking2ItemDto.getStatus(), actualBooking2ItemDto.getStatus());
    }

    @Test
    void toEntity() {
        Booking actualBooking = bookingMapper.toEntity(bookingDto);

        assertEquals(booking.getStart(), actualBooking.getStart());
        assertEquals(booking.getEnd(), actualBooking.getEnd());
        assertEquals(booking.getItem(), actualBooking.getItem());
    }

    @Test
    void toEntity_whenBookingDtoIsNull_thenReturnNull() {
        Booking actualBooking = bookingMapper.toEntity(null);

        assertNull(actualBooking);
    }

    @Test
    void mapToBooking2Dto() {
        when(itemRepository.findItemByIdOrderById(any()))
                .thenReturn(item);
        when(userRepository.findUserById(any()))
                .thenReturn(user);
        List<Booking2Dto> booking2Dtos = bookingMapper.mapToBooking2Dto(List.of(booking));

        assertEquals(booking2Dto.getId(), booking2Dtos.get(0).getId());
        assertEquals(booking2Dto.getStart(), booking2Dtos.get(0).getStart());
        assertEquals(booking2Dto.getEnd(), booking2Dtos.get(0).getEnd());
        assertEquals(booking2Dto.getItem(), booking2Dtos.get(0).getItem());
        assertEquals(booking2Dto.getBooker(), booking2Dtos.get(0).getBooker());
        assertEquals(booking2Dto.getStatus(), booking2Dtos.get(0).getStatus());
    }

    @Test
    void mapToItemBooking2Dto() {
        when(itemRepository.findItemByIdOrderById(any()))
                .thenReturn(item);
        when(userRepository.findUserById(any()))
                .thenReturn(user);
        List<Booking2ItemDto> booking2ItemList = bookingMapper.mapToItemBooking2Dto(List.of(booking));

        assertEquals(booking2ItemDto.getId(), booking2ItemList.get(0).getId());
        assertEquals(booking2ItemDto.getStart(), booking2ItemList.get(0).getStart());
        assertEquals(booking2ItemDto.getEnd(), booking2ItemList.get(0).getEnd());
        assertEquals(booking2ItemDto.getItem(), booking2ItemList.get(0).getItem());
        assertEquals(booking2ItemDto.getBookerId(), booking2ItemList.get(0).getBookerId());
        assertEquals(booking2ItemDto.getStatus(), booking2ItemList.get(0).getStatus());
    }
}