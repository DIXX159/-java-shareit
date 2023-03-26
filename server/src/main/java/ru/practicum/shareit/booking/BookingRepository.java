package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findBookingById(Long bookingId);

    @Transactional
    @Modifying
    @Query("update Booking " +
            "set status = :status " +
            "where id = :id")
    void updateBooking(@Param("id") Long id, @Param("status")BookingStatus status);

    Optional<List<Booking>> findBookingsByBookerAndItemAndStartBeforeAndEndBefore(Long bookerId, Long itemId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("select b from Booking as b " +
            "where b.booker = ?1 " +
            "order by b.start DESC")
    Page<Booking> findAllByBooker(Long bookerId, Pageable pageable);

    @Query("select b from Booking as b " +
            "where b.booker = ?1 and " +
            "?2 between b.start and b.end " +
            "group by b.id " +
            "order by b.start DESC")
    Page<Booking> findCurrentByBooker(Long bookerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select * from bookings as b " +
            "where booker_id = ?1 and " +
            "?2 < b.start_date " +
            "group by b.id " +
            "order by b.start_date DESC", nativeQuery = true)
    Page<Booking> findFutureByBooker(Long bookerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select * from bookings as b " +
            "where booker_id = ?1 and " +
            "?2 > b.end_date " +
            "group by b.id " +
            "order by b.start_date DESC", nativeQuery = true)
    Page<Booking> findPastByBooker(Long bookerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select * from bookings as b " +
            "where b.status = ?1 and b.booker_id = ?2 " +
            "group by b.id " +
            "order by b.start_date DESC", nativeQuery = true)
    Page<Booking> findAllBookingsByStatus(String state, Long bookerId, Pageable pageable);

    @Query(value = "select * from bookings as b " +
            "where b.item_id = ?1 " +
            "group by b.id " +
            "order by b.start_date ", nativeQuery = true)
    List<Booking> findAllBookingsByItem(Long itemId);

    @Query(value = "select * from bookings as b join items i on i.id = b.item_id " +
            "where i.owner_id = ?1 " +
            "group by b.id, i.id " +
            "order by b.start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingsByItemsOwner(Long ownerId, Pageable pageable);

    @Query(value = "select * from bookings as b join items i on i.id = b.item_id " +
            "where i.owner_id = ?1 and " +
            "?2 between start_date and end_date " +
            "group by b.id, i.id " +
            "order by b.start_date DESC", nativeQuery = true)
    List<Booking> findCurrentBookingsByItemsOwner(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select * from bookings as b join items i on i.id = b.item_id " +
            "where i.owner_id = ?1 and " +
            "?2 < b.start_date " +
            "group by b.id, i.id " +
            "order by b.start_date DESC", nativeQuery = true)
    List<Booking> findFutureBookingsByItemsOwner(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select * from bookings as b join items i on i.id = b.item_id " +
            "where i.owner_id = ?1 and " +
            "?2 > b.end_date " +
            "group by b.id, i.id " +
            "order by b.start_date DESC", nativeQuery = true)
    List<Booking> findPastBookingsByItemsOwner(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query(value = "select * from bookings as b join items i on i.id = b.item_id " +
            "where i.owner_id = ?1 and " +
            "?2 = b.status " +
            "group by b.id, i.id " +
            "order by b.start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingsByItemsOwnerStatus(Long ownerId, String state, Pageable pageable);
}