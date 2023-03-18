package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RequestRepositoryTest {

    @Autowired
    RequestRepository requestRepository;

    ItemRequest request1;
    ItemRequest request2;

    @BeforeEach
    public void addBookings() {
        requestRepository.deleteAll();
        request1 = new ItemRequest();
        request1.setId(1L);
        request1.setDescription("desc1");
        request1.setRequestorId(1L);
        request1.setCreated(LocalDateTime.now());
        requestRepository.save(request1);

        request2 = new ItemRequest();
        request2.setId(2L);
        request2.setDescription("desc2");
        request2.setRequestorId(2L);
        request2.setCreated(LocalDateTime.now());
        requestRepository.save(request2);
    }

    @Test
    void findItemRequestByDescription() {
        ItemRequest findRequest = requestRepository.findItemRequestByDescription("desc2");

        assertEquals(requestRepository.findAll().size(), 2);
        assertEquals(request2.getDescription(), findRequest.getDescription());
    }

    @Test
    void findItemRequestsByRequestorId() {
        List<ItemRequest> findRequests = requestRepository.findItemRequestsByRequestorId(2L);

        assertEquals(requestRepository.findAll().size(), 2);
        assertEquals(findRequests.size(), 1);
        assertEquals(request2.getDescription(), findRequests.get(0).getDescription());
    }

    @Test
    void findItemRequestById() {
        ItemRequest findRequest = requestRepository.findItemRequestById(2L);

        assertEquals(requestRepository.findAll().size(), 2);
        assertEquals(request2.getDescription(), findRequest.getDescription());
    }

    @Test
    void findItemRequestsByRequestorIdIsNot() {
        List<ItemRequest> findRequests = requestRepository.findItemRequestsByRequestorIdIsNot(2L, PageRequest.of(0, 20));

        assertEquals(requestRepository.findAll().size(), 2);
        assertEquals(findRequests.size(), 1);
        assertEquals(request1.getDescription(), findRequests.get(0).getDescription());
    }
}