package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserService {

    void add(User user);

    void delete(int id);

    void update(User updatedUser);

    User getUserById(int id);

    List<User> getAll();

    User findByUsername(String username);

}
