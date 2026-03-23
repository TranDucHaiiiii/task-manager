package com.example.task_manager.controller;

import com.example.task_manager.entity.User;
import org.springframework.web.bind.annotation.*;
import com.example.task_manager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "User", description = "User management APIs")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List users", description = "Get all users")
    public List<User> getAll() {
        return service.getAll();
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Create a user (not used for auth)")
    public User create(@RequestBody User user) {
        return service.create(user);
    }
}
