package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findItemByNameAndDescription(String name, String description);

    Optional<Item> findById(Long id);

    Item findItemByIdOrderById(Long id);

    List<Item> findAllByOwnerOrderById(Long userId, Pageable pageable);

    List<Item> findAllByOwnerOrderById(Long userId);

    List<Item> findItemsByRequestId(Long requestId);

    @Transactional
    @Modifying
    @Query(value = "update items " +
            "set name = :name, " +
            "description = :description, " +
            "is_available = :is_available, " +
            "owner_id = :owner_id, " +
            "request_id = :request_id " +
            "where id = :id", nativeQuery = true)
    void updateItem(@Param("id") Long id, @Param("name") String name, @Param("description") String description, @Param("is_available") Boolean available, @Param("owner_id") Long owner, @Param("request_id") Long requestId);

    @Query(value = "select i.id, i.id, i.name, i.description, i.is_available, i.owner_id, i.request_id from items as i order by i.id", nativeQuery = true)
    List<Item> findAllItems();

    List<Item> searchItemsByAvailableAndDescriptionContainsIgnoreCase(Boolean available, String text, Pageable pageable);
}
