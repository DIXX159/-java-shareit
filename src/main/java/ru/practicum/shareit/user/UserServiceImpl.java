package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ThrowableException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    Long userIdGenerator = 1L;
    HashMap<Long, User> users = new HashMap<>();
    List<String> email = new ArrayList<>();

    public User createUser (User user) throws ThrowableException, ValidationException {
        if (user.getEmail() != null){
        userRepository.save(user);
        return userRepository.findUserByEmail(user.getEmail());
        } else throw new ValidationException("Не указан e-mail");
    }

    @Override
    public User updateUser(Long userId, User user) throws ThrowableException {
        if (userRepository.findUserById(userId) != null) {
            user.setId(userId);
            if (user.getEmail() == null || !userRepository.findAllEmail().contains(user.getEmail())) {
                if (user.getEmail() == null) {
                    user.setEmail(userRepository.findUserById(userId).getEmail());
                }
                if (user.getName() == null) {
                    user.setName(userRepository.findUserById(userId).getName());
                }
                userRepository.updateUser(userId, user.getName(), user.getEmail());
                return userRepository.findUserById(userId);
            } else throw new ThrowableException(Constants.emailExist);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

        @Override
    public User getUserById(Long userId) {
        if (userRepository.findUserById(userId) != null) {
            return userRepository.findUserById(userId);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    @Override
    public void deleteUser(long userId) {
        if (userRepository.findUserById(userId) != null) {
            userRepository.deleteById(userId);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    /*
     @Override
    public User createUser(User user) throws ThrowableException, ValidationException {
        if (user.getEmail() != null) {
            if (!email.contains(user.getEmail())) {
                email.add(user.getEmail());
                user.setId(userIdGenerator++);
                users.put(user.getId(), user);
                return user;
            } else throw new ThrowableException(Constants.emailExist);
        } else throw new ValidationException("Не указан e-mail");
    }


    @Override
    public User updateUser(Long userId, User user) throws ThrowableException {
        if (users.get(userId) != null) {
            user.setId(userId);
            if (user.getEmail() == null || !email.contains(user.getEmail())) {
                email.add(user.getEmail());
                if (user.getEmail() != null) {
                    email.remove(users.get(userId).getEmail());
                }
                if (user.getEmail() == null) {
                    user.setEmail(users.get(userId).getEmail());
                }
                if (user.getName() == null) {
                    user.setName(users.get(userId).getName());
                }
                users.put(userId, user);
                return users.get(userId);
            } else throw new ThrowableException(Constants.emailExist);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    @Override
    public User getUserById(Long userId) {
        if (users.get(userId) != null) {
            return users.get(userId);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    @Override
    public void deleteUser(long userId) {
        if (users.get(userId) != null) {
            email.remove(users.get(userId).getEmail());
            users.remove(userId);
        } else throw new NotFoundException(Constants.userNotFound);
    }
    */

}