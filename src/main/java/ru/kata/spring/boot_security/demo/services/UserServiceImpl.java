package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    @Transactional(readOnly = true)
    public User show(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    @Transactional
    public void save(User user) {
        Optional<User> userFromDB = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
        if (!userFromDB.isEmpty()) {
            return;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    @Transactional
    public void update(Long id, User updatedUser) {
        User user = userRepository.findById((id)).orElse(null);
        if (user == null) {
            return;
        }
        user.setRoles(updatedUser.getRoles());
        user.setUsername(updatedUser.getUsername());
        user.setAge(updatedUser.getAge());
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().clear();
        userRepository.deleteById(id);
    }
}
