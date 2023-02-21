package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findUserByEmail(String email);
    public User findUserById(Long id);

    @Query("select u.email from User u")
    public List<String> findAllEmail();

    @Transactional
    @Modifying
    @Query(value = "update users " +
            "set name = :name, " +
            "email = :email " +
            "where id = :id", nativeQuery = true)
    void updateUser(@Param("id") Long id, @Param("name") String name, @Param("email") String email);
}
