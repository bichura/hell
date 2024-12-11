package ru.kata.spring.boot_security.demo.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           UserService userService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostConstruct
    public void initData() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        User adminUser = userRepository.findByUsername("admin");
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("admin");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Adminin");
            adminUser.setDateOfBirth(LocalDate.of(2000, 12, 16));
            adminUser.setRoles(List.of(adminRole, userRole));
            userService.saveUser(adminUser);
        }

        User regularUser = userRepository.findByUsername("user");
        if (regularUser == null) {
            regularUser = new User();
            regularUser.setUsername("user");
            regularUser.setPassword("user");
            regularUser.setFirstName("User");
            regularUser.setLastName("Userov");
            regularUser.setDateOfBirth(LocalDate.of(1991, 9, 7));
            regularUser.setRoles(List.of(userRole));
            userService.saveUser(regularUser);
        }
    }
}