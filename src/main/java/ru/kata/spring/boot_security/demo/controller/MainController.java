package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class MainController {
    private final UserService userService;
    private final RoleService roleService;

    public MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String startPageForAdmin(ModelMap model, Principal principal) {
        model.addAttribute("curUser", userService.findByUsername(principal.getName()));
        model.addAttribute("users", userService.getAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @GetMapping("/user")
    public String showUserInfo(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "user";
    }

    @PostMapping("/admin/saveUser")
    public String addUser(@ModelAttribute("newUser") @Valid User user) {
        userService.add(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/updateUser")
    public String updateUserInfo(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }
}