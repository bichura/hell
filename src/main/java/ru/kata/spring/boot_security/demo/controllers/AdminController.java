package ru.kata.spring.boot_security.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String startPage(Model model, Authentication authentication) throws JsonProcessingException {
        List<User> users = userService.getAllUsers();
        ObjectMapper objectMapper = new ObjectMapper();

        for (User user : users) {
            String rolesJson = objectMapper.writeValueAsString(user.getRoles());
            user.setRolesJson(rolesJson);
        }

        model.addAttribute("users", users);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("me", userService.findByEmail(authentication.getName()));
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @GetMapping("/roles")
    @ResponseBody
    public List<Map<String, Object>> getAllRoles() {
        return roleService.getAllRoles().stream()
                .map(role -> Map.of("id", role.getId(), "name", (Object) role.getName()))
                .collect(Collectors.toList());
    }

    @PostMapping("/new")
    public String saveUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/{id}")
    @ResponseBody
    public User getUserForEdit(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> editUser(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.parseLong(requestBody.get("id").toString());
        User user = userService.getUserById(userId);

        user.setFirstName(requestBody.get("firstName").toString());
        user.setLastName(requestBody.get("lastName").toString());
        user.setAge(Byte.parseByte(requestBody.get("age").toString()));
        user.setEmail(requestBody.get("email").toString());
        if (!requestBody.get("password").toString().isEmpty()) {
            user.setPassword(requestBody.get("password").toString());
        }

        List<Long> roleIds = (List<Long>) requestBody.get("roles");
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(roles);

        userService.editUser(user);
        return ResponseEntity.ok().body("{\"message\": \"User edited successfully\"}");
    }

    @PostMapping("/remove/{id}")
    @ResponseBody
    public ResponseEntity<String> removeUser(@PathVariable(value = "id") Long id) {
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        return ResponseEntity.ok("User deleted successfully");
    }
}