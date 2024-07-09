package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.security.Principal;
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

    @Override
    @Transactional
    public void add(User user) {
        Optional<User> userFromDB = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
        if (userFromDB.isPresent()) {
            return;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().clear();
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(User updatedUser) {
        User user = userRepository.findById(updatedUser.getId()).orElse(null);
        if (user == null) {
            return;
        }
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentuserDetails = (UserDetails) currentAuth.getPrincipal();
        if (((User) currentuserDetails).getId().equals(updatedUser.getId())) {
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedUser,
                    currentAuth.getCredentials(),
                    updatedUser.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        if (!updatedUser.getPassword().equals(user.getPassword())) {
            updatedUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
