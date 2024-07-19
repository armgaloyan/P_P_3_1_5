package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.List;

public interface RoleService {
    Role findByRole(String role);

    void save(Role role);

    List<Role> listRoles();
}
