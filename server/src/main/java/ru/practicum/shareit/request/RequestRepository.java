package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    ItemRequest findItemRequestByDescription(String description);

    List<ItemRequest> findItemRequestsByRequestorId(Long userId);

    ItemRequest findItemRequestById(Long requestId);

    List<ItemRequest> findItemRequestsByRequestorIdIsNot(Long ownerId, Pageable pageable);
}