package ru.kata.spring.boot_security.demo.user_add;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AddingUsers {
    private final UserService userService;

    public AddingUsers(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    @Transactional
    public void addUser() {
        User user2 = new User();
        user2.setUsername("user");
        user2.setAge(20);
        user2.setPassword("user");
        user2.setRoles(List.of(new Role("ROLE_USER")));
        userService.add(user2);
        User user = new User();
        user.setUsername("admin");
        user.setAge(20);
        user.setPassword("admin");
        user.setRoles(List.of(new Role("ROLE_ADMIN")));
        userService.add(user);
    }
}
