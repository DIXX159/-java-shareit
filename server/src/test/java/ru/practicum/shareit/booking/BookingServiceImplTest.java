package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking2Dto;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingMapper bookingMapper;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @Test
    @SneakyThrows
    void createBooking_whenBookingCreated_thenBookingSave() {
        Item item = new Item();
        item.setOwner(2L);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(1));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        BookingDto bookingDto = new BookingDto(
                null,
                LocalDateTime.now().withNano(0).plusMinutes(1),
                LocalDateTime.now().withNano(0).plusMinutes(2),
                1L,
                1L,
                "APPROVED"
        );

        when(bookingMapper.toEntity(bookingDto))
                .thenReturn(booking);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        Booking2Dto response = bookingService.createBooking(bookingDto, 1L);

        assertEquals(bookingMapper.toBookingDto(booking), response);
        verify(bookingRepository).save(booking);
    }

    @Test
    @SneakyThrows
    void createBooking_whenBookerIsOwner_thenNotFoundExceptionThrown() {
        Item item = new Item();
        item.setOwner(1L);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(1));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        BookingDto bookingDto = new BookingDto(
                null,
                LocalDateTime.now().withNano(0).plusMinutes(1),
                LocalDateTime.now().withNano(0).plusMinutes(2),
                1L,
                1L,
                "APPROVED"
        );
        when(bookingMapper.toEntity(bookingDto))
                .thenReturn(booking);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(bookingDto, 1L));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    @SneakyThrows
    void createBooking_whenItemNotAvailable_thenValidationExceptionThrown() {
        Item item = new Item();
        item.setOwner(2L);
        item.setAvailable(false);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(1));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        BookingDto bookingDto = new BookingDto(
                null,
                LocalDateTime.now().withNano(0).plusMinutes(1),
                LocalDateTime.now().withNano(0).plusMinutes(2),
                1L,
                1L,
                "APPROVED"
        );
        when(bookingMapper.toEntity(bookingDto))
                .thenReturn(booking);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookingDto, 1L));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    @SneakyThrows
    void createBooking_whenBookingStartIsAfterEnd_thenValidationExceptionThrown() {
        Item item = new Item();
        item.setOwner(2L);
        item.setAvailable(false);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        BookingDto bookingDto = new BookingDto(
                null,
                LocalDateTime.now().withNano(0).plusMinutes(1),
                LocalDateTime.now().withNano(0).plusMinutes(2),
                1L,
                1L,
                "APPROVED"
        );
        when(bookingMapper.toEntity(bookingDto))
                .thenReturn(booking);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.createBooking(bookingDto, 1L));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    @SneakyThrows
    void createBooking_whenItemNotFound_thenNotFoundExceptionThrown() {
        Booking booking = new Booking();
        BookingDto bookingDto = new BookingDto();

        when(bookingMapper.toEntity(bookingDto))
                .thenReturn(booking);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(bookingDto, 1L));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    @SneakyThrows
    void createBooking_whenUserNotFound_thenNotFoundExceptionThrown() {
        Booking booking = new Booking();
        BookingDto bookingDto = new BookingDto();

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(bookingDto, 1L));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    @SneakyThrows
    void updateApproving_whenApprovingUpdate_thenApprovingSave() {
        Item item = new Item();
        item.setOwner(1L);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        bookingService.updateApproving(1L, 1L, "true");

        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertEquals(BookingStatus.APPROVED, savedBooking.getStatus());
    }

    @Test
    @SneakyThrows
    void updateApproving_whenBookerIsNotOwner_thenNotFoundExceptionThrown() {
        Item item = new Item();
        item.setOwner(2L);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.updateApproving(1L, 1L, "true"));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    @SneakyThrows
    void updateApproving_whenBookerIsNotOwner_thenValidationExceptionThrown() {
        Item item = new Item();
        item.setOwner(1L);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.updateApproving(1L, 1L, "true"));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    @SneakyThrows
    void updateApproving_whenItemNotFound_thenNotFoundExceptionThrown() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.updateApproving(1L, 1L, "true"));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    @SneakyThrows
    void updateApproving_whenBookingNotFound_thenNotFoundExceptionThrown() {
        Booking booking = new Booking();

        assertThrows(NotFoundException.class, () -> bookingService.updateApproving(1L, 1L, "true"));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void getBookingById() {
        Item item = new Item();
        item.setOwner(1L);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        Booking2Dto response = bookingService.getBookingById(1L, 1L);

        assertEquals(bookingMapper.toBookingDto(booking), response);
        verify(bookingRepository).findBookingById(1L);
    }

    @Test
    void getBookingById_whenUserIsNotOwner_thenNotFoundExceptionThrown() {
        Item item = new Item();
        item.setOwner(2L);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
        verify(bookingRepository, never()).findBookingById(1L);
    }

    @Test
    void getBookingById_whenItemNotFound_thenNotFoundExceptionThrown() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
        verify(bookingRepository, never()).findBookingById(1L);
    }

    @Test
    void getBookingById_whenBookingNotFound_thenNotFoundExceptionThrown() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
        verify(bookingRepository, never()).findBookingById(1L);
    }

    @Test
    @SneakyThrows
    void getAllBookingsByUserId_whenStateAll_thenReturnBookings() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setBooker(1L);
        Page<Booking> page = new PageImpl<>(List.of(booking));
        Booking2Dto booking2Dto = new Booking2Dto(1L, LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0).plusMinutes(1), new Item(), new User(), BookingStatus.APPROVED);
        List<Booking2Dto> bookings = List.of(booking2Dto);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBooker(1L, PageRequest.of(0, 1)))
                .thenReturn(page);
        when(bookingMapper.mapToBooking2Dto(page))
                .thenReturn(bookings);

        List<Booking2Dto> response = bookingService.getAllBookingsByUserId("ALL", 1L, PageRequest.of(0, 1));

        assertEquals(bookings, response);
    }

    @Test
    void getAllBookingsByUserId_whenStateIsUnknown_thenThrowableExceptionThrown() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));

        assertThrows(ThrowableException.class, () -> bookingService.getAllBookingsByUserId("qwe", 1L, PageRequest.of(0, 1)));
        verify(bookingRepository, never()).findAllByBooker(1L, PageRequest.of(0, 1));
    }

    @Test
    void getAllBookingsByUserId_whenUserNotFound_thenNotFoundExceptionThrown() {
        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingsByUserId("qwe", 1L, PageRequest.of(0, 1)));
        verify(bookingRepository, never()).findAllByBooker(1L, PageRequest.of(0, 1));
    }

    @Test
    @SneakyThrows
    void getAllBookingsItemsByUserId_whenStateAll_thenReturnBookings() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().withNano(0).plusMinutes(3));
        booking.setEnd(LocalDateTime.now().withNano(0).plusMinutes(2));
        booking.setItem(1L);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setBooker(1L);
        Booking2Dto booking2Dto = new Booking2Dto(1L, LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0).plusMinutes(1), new Item(), new User(), BookingStatus.APPROVED);
        List<Booking2Dto> bookings = List.of(booking2Dto);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllBookingsByItemsOwner(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(booking));
        when(bookingMapper.mapToBooking2Dto(List.of(booking)))
                .thenReturn(bookings);
        when(itemRepository.findAllByOwnerOrderById(1L))
                .thenReturn(List.of(new Item()));

        List<Booking2Dto> response = bookingService.getAllBookingsItemsByUserId(1L, "ALL", PageRequest.of(0, 1));

        assertEquals(bookings, response);
    }

    @Test
    void getAllBookingsItemsByUserId_whenStateIsUnknown_thenThrowableExceptionThrown() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));
        when(itemRepository.findAllByOwnerOrderById(1L))
                .thenReturn(List.of(new Item()));

        assertThrows(ThrowableException.class, () -> bookingService.getAllBookingsItemsByUserId(1L, "qwe", PageRequest.of(0, 1)));
        verify(bookingRepository, never()).findAllBookingsByItemsOwner(1L, PageRequest.of(0, 1));
    }

    @Test
    void getAllBookingsItemsByUserId_whenItemsNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User()));

        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingsItemsByUserId(1L, "qwe", PageRequest.of(0, 1)));
        verify(bookingRepository, never()).findAllBookingsByItemsOwner(1L, PageRequest.of(0, 1));
    }

    @Test
    void getAllBookingsItemsByUserId_whenUserNotFound_thenNotFoundExceptionThrown() {
        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingsItemsByUserId(1L, "qwe", PageRequest.of(0, 1)));
        verify(bookingRepository, never()).findAllBookingsByItemsOwner(1L, PageRequest.of(0, 1));
    }
}