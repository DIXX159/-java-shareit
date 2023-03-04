package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking2ItemDto;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto createItem(Item item, Long userId) throws Throwable {
        if (userId > 0) {
            if (item.getAvailable() != null) {
                if (userRepository.findUserById(userId) != null) {
                    if (itemRepository.findItemByNameAndDescription(item.getName(), item.getDescription()) != null) {
                        throw new ThrowableException("Вещь уже существует");
                    }
                    item.setOwner(userRepository.findUserById(userId).getId());
                    itemRepository.save(item);
                    return itemMapper.toItemDto(itemRepository.findItemByNameAndDescription(item.getName(), item.getDescription()));
                }
                throw new NotFoundException(Constants.userNotFound);
            } else throw new ValidationException("Отсутствует статус");
        } else throw new ThrowableException(Constants.idNotFound);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) throws ThrowableException {
        Item item = itemMapper.toEntity(itemDto);
        if (userId > 0) {
            if (item.getId() == null) {
                item.setId(itemId);
            }
            Item updateItem = itemRepository.findItemByIdOrderById(itemId);
            if (Objects.equals(updateItem.getOwner(), userId)) {
                if (item.getName() == null) {
                    item.setName(updateItem.getName());
                }
                if (item.getDescription() == null) {
                    item.setDescription(updateItem.getDescription());
                }
                if (item.getOwner() == null) {
                    item.setOwner(userId);
                }
                if (item.getAvailable() == null) {
                    item.setAvailable(updateItem.getAvailable());
                }
                if (item.getRequest() == null) {
                    item.setRequest(updateItem.getRequest());
                }
                itemRepository.updateItem(itemId, item.getName(), item.getDescription(), item.getAvailable(), item.getOwner(), 0L);
                itemRepository.saveAndFlush(item);
                return itemMapper.toItemDto(itemRepository.findItemByIdOrderById(updateItem.getId()));
            } else throw new NotFoundException("Пользователь не является владельцем");
        } else throw new ThrowableException(Constants.idNotFound);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        List<Item> itemsByUserId = itemRepository.findAllByOwnerOrderById(userId);
        if (!itemsByUserId.isEmpty()) {
            List<ItemDto> items = itemMapper.mapToItemDto(itemsByUserId);
            for (ItemDto i : items) {
                addBookings(bookingMapper.mapToItemBooking2Dto(bookingRepository.findAllBookingsByItem(i.getId())), i,
                        commentMapper.mapToCommentDto(commentRepository.findCommentsByItem(i.getId())), userId);
            }
            return items;
        } else throw new NotFoundException("У пользователя нет вещей");
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<ItemDto> items = itemMapper.mapToItemDto(itemRepository.findAllItems());
        for (ItemDto i : items) {
            addBookings(bookingMapper.mapToItemBooking2Dto(bookingRepository.findAllBookingsByItem(i.getId())), i,
                    commentMapper.mapToCommentDto(commentRepository.findCommentsByItem(i.getId())), i.getOwner());
        }
        return items;
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.itemNotFound));
        return addBookings(bookingMapper.mapToItemBooking2Dto(bookingRepository.findAllBookingsByItem(itemId)),
                itemMapper.toItemDto(item),
                commentMapper.mapToCommentDto(commentRepository.findCommentsByItem(itemId)), userId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (!text.isEmpty()) {
            return itemMapper.mapToItemDto(itemRepository.searchItemsByAvailableAndAndDescriptionContainsIgnoreCase(true, text.toLowerCase()));
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteItem(long itemId) {
        if (itemRepository.findItemByIdOrderById(itemId) != null) {
            itemRepository.deleteById(itemId);
        } else throw new NotFoundException(Constants.itemNotFound);
    }

    @Override
    public CommentDto createComment(Comment comment, Long itemId, Long userId) throws ValidationException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(Constants.itemNotFound));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.userNotFound));
        Booking booking = bookingRepository.findBookingByBookerAndItemAndStartBefore(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException(Constants.bookingNotFound));
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            comment.setItem(itemId);
            comment.setAuthor(userId);
            comment.setCreated(LocalDateTime.now());
            commentRepository.save(comment);
            return commentMapper.toCommentDto(commentRepository.findByItemAndAuthor(itemId, userId));
        } else throw new ValidationException("Срок аренды еще не закончился");
    }

    public ItemDto addBookings(List<Booking2ItemDto> bookings, ItemDto item, List<CommentDto> comments, Long userId) {
        if (!bookings.isEmpty()) {
            for (int i = 0; i < bookings.size(); i++) {
                if (!Objects.equals(bookings.get(i).getStatus(), "REJECTED")) {
                    if (LocalDateTime.parse(bookings.get(i).getEnd()).isBefore(LocalDateTime.now())) {
                        if (Objects.equals(item.getOwner(), userId)) {
                            item.setLastBooking(bookings.get(i));
                            item.setNextBooking(bookings.get(i + 1));
                        }
                    }
                }
            }
            if (comments != null) {
                item.setComments(comments);
            }
        }
        return item;
    }
}