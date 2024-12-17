package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
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

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String startPage(Model model, Authentication authentication) {
        model.addAttribute("users", userService.getAllUsers());
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

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        User user = userService.getUserById(id);
        user.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping("/edit")
    @ResponseBody
//    public String editUser(@RequestBody User user) {
//        userService.editUser(user);
//        return "redirect:/admin";
//    }
    public ResponseEntity<String> editUser(@RequestBody User user) {
        userService.editUser(user);
        return ResponseEntity.ok("User edited successfully");
    }

    @PostMapping("/remove/{id}")
    @ResponseBody
//    public ResponseEntity<String> removeUser(@PathVariable(value = "id") Long id) {
//        User user = userService.getUserById(id);
//        userService.deleteUser(user);
//        return ResponseEntity.ok("User deleted successfully");
//    }
    public ResponseEntity<String> removeUser(@PathVariable(value = "id") Long id) {
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        return ResponseEntity.ok("User deleted successfully");
    }
}
