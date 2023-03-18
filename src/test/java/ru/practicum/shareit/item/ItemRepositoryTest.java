package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    Item item1;
    Item item2;

    @BeforeEach
    public void addBookings() {
        itemRepository.deleteAll();
        item1 = new Item();
        item1.setId(1L);
        item1.setName("item1");
        item1.setDescription("desc1");
        item1.setAvailable(true);
        item1.setOwner(1L);
        item1.setRequestId(1L);
        itemRepository.save(item1);

        item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setDescription("desc2");
        item2.setAvailable(true);
        item2.setOwner(2L);
        item2.setRequestId(2L);
        itemRepository.save(item2);
    }

    @Test
    void findItemByNameAndDescription() {
        Item findItem = itemRepository.findItemByNameAndDescription("item1", "desc1");

        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(item1.getName(), findItem.getName());
        assertEquals(item1.getDescription(), findItem.getDescription());
    }

    @Test
    void findById() {
        Optional<Item> findItem = itemRepository.findById(2L);

        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(item2.getName(), findItem.get().getName());
        assertEquals(item2.getDescription(), findItem.get().getDescription());
    }

    @Test
    void findItemByIdOrderById() {
        Item findItem = itemRepository.findItemByIdOrderById(2L);

        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(item2.getName(), findItem.getName());
        assertEquals(item2.getDescription(), findItem.getDescription());
    }

    @Test
    void findAllByOwnerOrderById() {
        List<Item> findItems = itemRepository.findAllByOwnerOrderById(2L, PageRequest.of(0,20));

        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(findItems.size(), 1);
        assertEquals(item2.getName(), findItems.get(0).getName());
    }

    @Test
    void testFindAllByOwnerOrderById() {
        List<Item> findItems = itemRepository.findAllByOwnerOrderById(2L);

        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(findItems.size(), 1);
        assertEquals(item2.getName(), findItems.get(0).getName());
    }

    @Test
    void findItemsByRequestId() {
        List<Item> findItems = itemRepository.findItemsByRequestId(2L);

        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(findItems.size(), 1);
        assertEquals(item2.getName(), findItems.get(0).getName());
    }

    @Test
    void updateItem() {
        itemRepository.updateItem(2L, "item3", "desc3", true, 2L, 2L);
        item2.setName("item3");
        item2.setDescription("desc3");
        item2.setAvailable(true);
        item2.setOwner(2L);
        item2.setRequestId(2L);
        itemRepository.save(item2);

        List<Item> findItems = itemRepository.findAll();

        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(findItems.size(), 2);
        assertEquals(item2.getId(), findItems.get(1).getId());
        assertEquals("item3", findItems.get(1).getName());
    }

    @Test
    void findAllItems() {
        List<Item> findItems = itemRepository.findAllItems();

        assertEquals(itemRepository.findAll().size(), 2);
        assertEquals(findItems.size(), 2);
        assertEquals(item2.getId(), findItems.get(1).getId());
        assertEquals("item2", findItems.get(1).getName());
    }

    @Test
    void searchItemsByAvailableAndAndDescriptionContainsIgnoreCase() {
        List<Item> findItems = itemRepository.searchItemsByAvailableAndDescriptionContainsIgnoreCase(true, "desc", PageRequest.of(0,20));

        List<Item> all = itemRepository.findAll();
        assertEquals(all.size(), 2);
        assertEquals(findItems.size(), 2);
        assertEquals(item2.getId(), findItems.get(1).getId());
        assertEquals("item2", findItems.get(1).getName());
    }
}