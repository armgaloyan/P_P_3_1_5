package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "showAllUsers";
    }
    @GetMapping("/show")
    public String showById(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.show(id));
        model.addAttribute("roles", roleRepository.findAll());
        return "showById";
    }
    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "new";
    }
    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user,
                         @RequestParam(value = "roles", required = false) List<Long> roleIds,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new";
        }

        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> roles = roleRepository.findAllById(roleIds);
            user.setRoles(new HashSet<>(roles));
        }

        userService.save(user);
        return "redirect:/admin";
    }
    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") Long id) {
        model.addAttribute("user", userService.show(id));
        model.addAttribute("roles", roleRepository.findAll());
        return "edit";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam("id") Long id,
                         @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        if (bindingResult.hasErrors())
            return "edit";

        if (roleIds != null) {
            List<Role> roles = roleRepository.findAllById(roleIds);
            user.setRoles(new HashSet<>(roles));
        }

        userService.update(id, user);
        return "redirect:/admin";
    }
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
