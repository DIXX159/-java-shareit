package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking2Dto;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Booking2Dto createBooking(Booking booking, Long bookerId) throws ValidationException {
        User user = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException(Constants.userNotFound));
        Item item = itemRepository.findById(booking.getItem())
                .orElseThrow(() -> new NotFoundException(Constants.itemNotFound));
        if (!Objects.equals(item.getOwner(), bookerId)) {
            if (item.getAvailable()) {
                if (booking.getStart().isBefore(booking.getEnd())) {
                    booking.setBooker(bookerId);
                    booking.setStatus(BookingStatus.WAITING);
                    bookingRepository.save(booking);
                    return bookingMapper.toBookingDto(bookingRepository.findBookingByBookerAndItemAndStart(bookerId, booking.getItem(), booking.getStart()));
                } else throw new ValidationException("Некорректное время бронирования");
            } else throw new ValidationException(Constants.itemNotAvailable);
        } else throw new NotFoundException("Пользователь является владельцем");
    }

    @Override
    public Booking2Dto updateApproving(Long bookingId, Long ownerId, String approved) throws ValidationException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Constants.bookingNotFound));
        Item item = itemRepository.findById(booking.getItem())
                .orElseThrow(() -> new NotFoundException(Constants.itemNotFound));
        if (!Objects.equals(booking.getStatus(), BookingStatus.APPROVED)) {
            if (Objects.equals(item.getOwner(), ownerId)) {
                if (Objects.equals(approved, "true")) {
                    bookingRepository.updateBooking(bookingId, "APPROVED");
                    booking.setStatus(BookingStatus.APPROVED);
                } else {
                    bookingRepository.updateBooking(bookingId, "REJECTED");
                    booking.setStatus(BookingStatus.REJECTED);
                }
                bookingRepository.saveAndFlush(booking);
                return bookingMapper.toBookingDto(bookingRepository.findBookingById(bookingId));
            } else throw new NotFoundException("Пользователь не является владельцем");
        } else throw new ValidationException("Бронирование уже одобрено");
    }

    @Override
    public Booking2Dto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Constants.bookingNotFound));
        Item item = itemRepository.findById(booking.getItem())
                .orElseThrow(() -> new NotFoundException(Constants.itemNotFound));
        if (Objects.equals(item.getOwner(), userId) || Objects.equals(booking.getBooker(), userId)) {
            return bookingMapper.toBookingDto(bookingRepository.findBookingById(bookingId));
        } else throw new NotFoundException("Пользователь не является владельцем или арендатором");
    }

    @Override
    public List<Booking2Dto> getAllBookingsByUserId(String state, Long userId) throws ThrowableException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.userNotFound));
        if (state == null || state.equals("ALL")) {
            return bookingMapper.mapToBooking2Dto(bookingRepository.findAllByBooker(userId));
        }
        if (state.equals("CURRENT")) {
            return bookingMapper.mapToBooking2Dto(bookingRepository.findCurrentByBooker(userId, LocalDateTime.now()));
        }
        if (state.equals("FUTURE")) {
            return bookingMapper.mapToBooking2Dto(bookingRepository.findFutureByBooker(userId, LocalDateTime.now()));
        }
        if (state.equals("PAST")) {
            return bookingMapper.mapToBooking2Dto(bookingRepository.findPastByBooker(userId, LocalDateTime.now()));
        }
        if (state.equals("WAITING") || state.equals("REJECTED")) {
            return bookingMapper.mapToBooking2Dto(bookingRepository.findAllBookingsByStatus(state, userId));
        } else throw new ThrowableException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public List<Booking2Dto> getAllBookingsItemsByUserId(Long ownerId, String state) throws ThrowableException {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(Constants.userNotFound));
        List<Item> items = itemRepository.findAllByOwnerOrderById(ownerId);

        if (!items.isEmpty()) {
            if (state == null || state.equals("ALL")) {
                return bookingMapper.mapToBooking2Dto(bookingRepository.findAllBookingsByItemsOwner(ownerId));
            }
            if (state.equals("CURRENT")) {
                return bookingMapper.mapToBooking2Dto(bookingRepository.findCurrentBookingsByItemsOwner(ownerId, LocalDateTime.now()));
            }
            if (state.equals("FUTURE")) {
                return bookingMapper.mapToBooking2Dto(bookingRepository.findFutureBookingsByItemsOwner(ownerId, LocalDateTime.now()));
            }
            if (state.equals("PAST")) {
                return bookingMapper.mapToBooking2Dto(bookingRepository.findPastBookingsByItemsOwner(ownerId, LocalDateTime.now()));
            }
            if (state.equals("WAITING") || state.equals("REJECTED")) {
                return bookingMapper.mapToBooking2Dto(bookingRepository.findAllBookingsByItemsOwnerStatus(ownerId, state));
            } else throw new ThrowableException("Unknown state: UNSUPPORTED_STATUS");
        } else throw new NotFoundException("У пользователя нет вещей");
    }
}