package ru.kata.spring.boot_security.demo.user_add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserAdd {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserAdd(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void postConstruct() {

        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");
        List<Role> listAdmin = new ArrayList<>();
        listAdmin.add(admin);
        List<Role> listUser = new ArrayList<>();
        listUser.add(user);
        List<Role> listUserAndAdmin = new ArrayList<>();
        listUserAndAdmin.add(user);
        listUserAndAdmin.add(admin);
        roleService.save(admin);
        roleService.save(user);

        User userAdmin = new User("admin",42,"admin", listAdmin);
        User userUser = new User("user", 19,"user", listUser);
        User userUserAndAdmin = new User("useradmin", 50,"useradmin",listUserAndAdmin);
        userService.add(userAdmin);
        userService.add(userUser);
        userService.add(userUserAndAdmin);

    }
}