package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;

    @BeforeEach
    public void addBookings() {
        userRepository.deleteAll();
        user1 = new User();
        user1.setId(1L);
        user1.setName("name1");
        user1.setEmail("user1@mail.ru");
        userRepository.save(user1);

        user2 = new User();
        user2.setId(2L);
        user2.setName("name2");
        user2.setEmail("user2@mail.ru");
        userRepository.save(user2);
    }

    @Test
    void findUserByEmail() {
        User findUser = userRepository.findUserByEmail("user2@mail.ru");

        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(user2.getEmail(), findUser.getEmail());
    }

    @Test
    void findUserById() {
        User findUser = userRepository.findUserById(2L);

        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(user2.getEmail(), findUser.getEmail());
    }

    @Test
    void findById() {
        Optional<User> findUser = userRepository.findById(2L);

        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(user2.getEmail(), findUser.get().getEmail());
    }

    @Test
    void findByEmail() {
        Optional<User> findUser = userRepository.findByEmail("user2@mail.ru");

        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(user2.getEmail(), findUser.get().getEmail());
    }

    @Test
    void findAllEmail() {
        List<String> emails = userRepository.findAllEmail();

        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(emails.size(), 2);
        assertEquals(user2.getEmail(), emails.get(1));
    }

    @Test
    void updateUser() {
        userRepository.updateUser(2L, "name3", "user3@mail.ru");
        user2.setName("name3");
        user2.setEmail("user3@mail.ru");
        userRepository.save(user2);

        List<User> findUsers = userRepository.findAll();

        assertEquals(userRepository.findAll().size(), 2);
        assertEquals(findUsers.size(), 2);
        assertEquals(user2.getId(), findUsers.get(1).getId());
        assertEquals("name3", findUsers.get(1).getName());
    }
}