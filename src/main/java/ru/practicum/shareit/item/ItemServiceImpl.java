package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserService userService;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    Long itemIdGenerator = 1L;
    HashMap<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(Item item, Long userId) throws Throwable {
        if (userId > 0) {
            if (item.getAvailable() != null) {
                if (userRepository.findUserById(userId) != null){
                    item.setOwner(userRepository.findUserById(userId).getId());
                    itemRepository.save(item);
                    return itemRepository.findItemByNameAndDescription(item.getName(), item.getDescription());
                } throw new NotFoundException(Constants.userNotFound);
            } else throw new ValidationException("Отсутствует статус");
        } else throw new ThrowableException(Constants.idNotFound);
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long userId) throws ThrowableException {
        if (userId > 0) {
            if (item.getId() == null) {
                item.setId(itemId);
            }
            Item updateItem = itemRepository.findItemById(itemId);
            if (updateItem.getOwner() == userId) {
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
                itemRepository.updateItem(itemId, item.getName(), item.getDescription(), item.getAvailable().toString(), item.getOwner(), 0L);
                return itemRepository.findItemById(itemId);
            } else throw new NotFoundException("Пользователь не является владельцем");
        } else throw new ThrowableException(Constants.idNotFound);
    }

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        List<Item> itemsByUserId = itemRepository.findAllByOwner(userId);
        if (!itemsByUserId.isEmpty()) {
            return itemsByUserId;
        } else throw new NotFoundException("У пользователя нет вещей");
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Long itemId) {
        if (itemRepository.findItemById(itemId) != null) {
            return itemRepository.findItemById(itemId);
        } else throw new NotFoundException(Constants.itemNotFound);
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> searchingItems = new ArrayList<>();
        if (!text.isEmpty()) {
            for (long i = 1; i <= itemRepository.findAll().size(); i++) {
                Item item = itemRepository.findItemById(i);
                if (item.getAvailable() && item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getAvailable() && item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    searchingItems.add(item);
                }
            }
        }
        return searchingItems;
    }

    @Override
    public void deleteItem(long itemId) {
        if (itemRepository.findItemById(itemId) != null) {
            itemRepository.findItemById(itemId);
        } else throw new NotFoundException(Constants.itemNotFound);
    }

    /*@Override
    public Item createItem(Item item, Long userId) throws Throwable {
        if (userId > 0) {
            if (item.getAvailable() != null) {
                try {
                    item.setOwner(userService.getUserById(userId).getId());
                    item.setId(itemIdGenerator++);
                    items.put(item.getId(), item);
                    return item;
                } catch (NotFoundException e) {
                    throw new NotFoundException(Constants.userNotFound);
                }
            } else throw new ValidationException("Отсутствует статус");
        } else throw new ThrowableException(Constants.idNotFound);
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long userId) throws ThrowableException {
        if (userId > 0) {
            if (item.getId() == null) {
                item.setId(itemId);
            }
            if (userId.equals(getItemById(itemId).getOwner())) {
                if (item.getName() == null) {
                    item.setName(getItemById(itemId).getName());
                }
                if (item.getDescription() == null) {
                    item.setDescription(getItemById(itemId).getDescription());
                }
                if (item.getOwner() == null) {
                    item.setOwner(userId);
                }
                if (item.getAvailable() == null) {
                    item.setAvailable(getItemById(itemId).getAvailable());
                }
                if (item.getRequest() == null) {
                    item.setRequest(getItemById(itemId).getRequest());
                }
                items.put(itemId, item);
                return items.get(itemId);
            } else throw new NotFoundException("Пользователь не является владельцем");
        } else throw new ThrowableException(Constants.idNotFound);
    }

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        List<Item> itemsByUserId = new ArrayList<>();
        for (long i = 1; i <= items.size(); i++) {
            if (Objects.equals(items.get(i).getOwner(), userId)) {
                itemsByUserId.add(items.get(i));
            }
        }
        if (!itemsByUserId.isEmpty()) {
            return itemsByUserId;
        } else throw new NotFoundException("У пользователя нет вещей");
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getItemById(Long itemId) {
        if (items.get(itemId) != null) {
            return items.get(itemId);
        } else throw new NotFoundException(Constants.itemNotFound);
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> searchingItems = new ArrayList<>();
        if (!text.isEmpty()) {
            for (long i = 1; i <= items.size(); i++) {
                Item item = items.get(i);
                if (item.getAvailable() && item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getAvailable() && item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    searchingItems.add(item);
                }
            }
        }
        return searchingItems;
    }

    @Override
    public void deleteItem(long itemId) {
        if (items.get(itemId) != null) {
            items.remove(itemId);
        } else throw new NotFoundException(Constants.itemNotFound);
    }
     */
}