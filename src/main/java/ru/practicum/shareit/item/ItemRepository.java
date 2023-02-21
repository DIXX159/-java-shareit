package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    public Item findItemByOwner(Long userId);

    public Item findItemByNameAndDescription(String name, String description);

    public Item findItemById(Long id);

    public List<Item> findAllByOwner(Long userId);

    @Transactional
    @Modifying
    @Query(value = "update items " +
            "set name = :name, " +
            "description = :description, " +
            "is_available = :is_available, " +
            "owner_id = :owner_id, " +
            "request_id = :request_id " +
            "where id = :id", nativeQuery = true)
    void updateItem(@Param("id") Long id, @Param("name") String name, @Param("description") String description, @Param("is_available") String available, @Param("owner_id") Long owner, @Param("request_id") Long request);

}