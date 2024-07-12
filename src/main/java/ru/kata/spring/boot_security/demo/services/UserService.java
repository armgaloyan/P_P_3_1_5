package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserService {
    void add(User user);

    void delete(Long id);

    void update(User user);

    List<User> getAll();

    User getUserById(Long id);

    User findByUsername(String username);

    User getCurrentUser();
}
