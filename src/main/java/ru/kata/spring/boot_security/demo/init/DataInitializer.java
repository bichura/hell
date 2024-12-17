package ru.kata.spring.boot_security.demo.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

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
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        User regularUser = userRepository.findByEmail("user@gmail.com");
        if (regularUser == null) {
            regularUser = new User();
            regularUser.setEmail("user@gmail.com");
            regularUser.setPassword("user");
            regularUser.setFirstName("User");
            regularUser.setLastName("Userov");
            regularUser.setAge((byte) 33);
            regularUser.setRoles(List.of(userRole));
            userService.saveUser(regularUser);
        }

        User adminUser = userRepository.findByEmail("admin@gmail.com");
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword("admin");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Adminin");
            adminUser.setAge((byte) 24);
            adminUser.setRoles(List.of(adminRole, userRole));
            userService.saveUser(adminUser);
        }
    }
}