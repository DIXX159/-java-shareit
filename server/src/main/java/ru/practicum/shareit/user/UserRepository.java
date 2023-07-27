package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    User findUserById(Long id);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    @Query("select u.email from User u")
    List<String> findAllEmail();

    @Transactional
    @Modifying
    @Query("update User " +
            "set name = :name, " +
            "email = :email " +
            "where id = :id")
    void updateUser(@Param("id") Long id, @Param("name") String name, @Param("email") String email);
}