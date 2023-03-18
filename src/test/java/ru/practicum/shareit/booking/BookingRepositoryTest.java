package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Booking booking;

    @BeforeEach
    public void addBookings() {
        bookingRepository.deleteAll();
        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(2));
        booking.setItem(4L);
        booking.setBooker(1L);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
    }

    @Test
    void findBookingById() {
        Booking bookingById = bookingRepository.findBookingById(1L);

        assertNotNull(bookingById);
    }

    @Test
    void updateBooking() {
        bookingRepository.updateBooking(1L, "REJECTED");
        booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        Booking bookingById = bookingRepository.findBookingById(1L);
        assertEquals(bookingById.getStatus(), BookingStatus.REJECTED);
    }

    @Test
    void findBookingsByBookerAndItemAndStartBeforeAndEndBefore() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(1L);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        Optional<List<Booking>> bookings = bookingRepository.findBookingsByBookerAndItemAndStartBeforeAndEndBefore(1L, 1L, LocalDateTime.now(), LocalDateTime.now());

        assertEquals(bookings.get().size(), 1);
        assertEquals(bookings.get().get(0).getId(), 2);
    }

    @Test
    void findAllByBooker() {
        Page<Booking> bookingsByBooker = bookingRepository.findAllByBooker(1L, PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 1);
        assertEquals(bookingsByBooker.getSize(), 20);
        assertEquals(bookingsByBooker.getTotalElements(), 1);
        assertEquals(bookingsByBooker.getContent().get(0).getId(), 1);
    }

    @Test
    void findCurrentByBooker() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().plusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(1L);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        Page<Booking> bookingsByBooker = bookingRepository.findCurrentByBooker(1L, LocalDateTime.now(), PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.getSize(), 20);
        assertEquals(bookingsByBooker.getTotalElements(), 1);
        assertEquals(bookingsByBooker.getContent().get(0).getId(), 2);
    }

    @Test
    void findFutureByBooker() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().plusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(1L);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        Page<Booking> bookingsByBooker = bookingRepository.findFutureByBooker(1L, LocalDateTime.now(), PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.getSize(), 20);
        assertEquals(bookingsByBooker.getTotalElements(), 1);
        assertEquals(bookingsByBooker.getContent().get(0).getId(), 1);
    }

    @Test
    void findPastByBooker() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(1L);
        booking2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking2);

        Page<Booking> bookingsByBooker = bookingRepository.findPastByBooker(1L, LocalDateTime.now(), PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.getSize(), 20);
        assertEquals(bookingsByBooker.getTotalElements(), 1);
        assertEquals(bookingsByBooker.getContent().get(0).getId(), 2);
    }

    @Test
    void findAllBookingsByStatus() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(1L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);

        Page<Booking> bookingsByBooker = bookingRepository.findAllBookingsByStatus("REJECTED", 1L, PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.getSize(), 20);
        assertEquals(bookingsByBooker.getTotalElements(), 1);
        assertEquals(bookingsByBooker.getContent().get(0).getId(), 2);
        assertEquals(bookingsByBooker.getContent().get(0).getStatus(), BookingStatus.REJECTED);
    }

    @Test
    void findAllBookingsByItem() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        booking2.setItem(2L);
        booking2.setBooker(1L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);

        List<Booking> bookingsByBooker = bookingRepository.findAllBookingsByItem(2L);

        List<Booking> all = bookingRepository.findAll();
        assertEquals(all.size(), 2);
        assertEquals(bookingsByBooker.size(), 1);
        assertEquals(bookingsByBooker.get(0).getId(), 2);
        assertEquals(bookingsByBooker.get(0).getItem(), 2);
    }

    @Test
    void findAllBookingsByItemsOwner() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(2L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(3L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        itemRepository.save(item);


        List<Booking> bookingsByBooker = bookingRepository.findAllBookingsByItemsOwner(3L, PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.size(), 1);
        assertEquals(bookingsByBooker.get(0).getId(), 2);
        assertEquals(bookingsByBooker.get(0).getItem(), 1);
    }

    @Test
    void findCurrentBookingsByItemsOwner() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().plusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(2L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(3L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        itemRepository.save(item);

        List<Booking> bookingsByBooker = bookingRepository.findCurrentBookingsByItemsOwner(3L, LocalDateTime.now(), PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.size(), 1);
        assertEquals(bookingsByBooker.get(0).getId(), 2);
        assertEquals(bookingsByBooker.get(0).getItem(), 1);
    }


    @Test
    void findFutureBookingsByItemsOwner() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().plusHours(1));
        booking2.setEnd(LocalDateTime.now().plusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(2L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(3L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        itemRepository.save(item);

        List<Booking> bookingsByBooker = bookingRepository.findFutureBookingsByItemsOwner(3L, LocalDateTime.now(), PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.size(), 1);
        assertEquals(bookingsByBooker.get(0).getId(), 2);
        assertEquals(bookingsByBooker.get(0).getItem(), 1);
    }

    @Test
    void findPastBookingsByItemsOwner() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(2L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(3L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        itemRepository.save(item);

        List<Booking> bookingsByBooker = bookingRepository.findPastBookingsByItemsOwner(3L, LocalDateTime.now(), PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.size(), 1);
        assertEquals(bookingsByBooker.get(0).getId(), 2);
        assertEquals(bookingsByBooker.get(0).getItem(), 1);
    }

    @Test
    void findAllBookingsByItemsOwnerStatus() {
        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        booking2.setItem(1L);
        booking2.setBooker(2L);
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);
        Item item = new Item();
        item.setId(1L);
        item.setOwner(3L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        itemRepository.save(item);

        List<Booking> bookingsByBooker = bookingRepository.findAllBookingsByItemsOwnerStatus(3L, "REJECTED", PageRequest.of(0, 20));

        assertEquals(bookingRepository.findAll().size(), 2);
        assertEquals(bookingsByBooker.size(), 1);
        assertEquals(bookingsByBooker.get(0).getId(), 2);
        assertEquals(bookingsByBooker.get(0).getItem(), 1);
    }

    @AfterEach
    public void deleteBooking() {
        bookingRepository.deleteAll();
    }
}