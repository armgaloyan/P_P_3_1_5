package ru.kata.spring.boot_security.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService, RoleRepository roleRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    /**
     * RestController for User page about user information
     **/
    @GetMapping("/user")
    public ResponseEntity<User> showUserInfo() {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }


    /**
     * RestControllers for Admin with CRUD operation and information about all users
     **/
    @GetMapping(value = "/admin")
    public ResponseEntity<List<User>> startPageForAdmin() {
        List<User> userList = new ArrayList<>();
        for (User user : userService.getAll()) {
            userList.add(user);
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<User> showUser(@RequestParam Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/admin/saveUser")
    public ResponseEntity<HttpStatus> addUser(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.add(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/admin/deleteUser")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/admin/updateUser")
    public ResponseEntity<HttpStatus> updateUserInfo(@RequestBody @NotNull User user, @RequestParam Long id) {
        if (user.getRoles() == null || user.getRoles().size() == 0) {
            user.setRoles(userService.getUserById(id).getRoles());
        }
        user.setId(id);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}