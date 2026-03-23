package com.example.task_manager.controller;

import com.example.task_manager.entity.User;
import org.springframework.web.bind.annotation.*;
import com.example.task_manager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return service.create(user);
    }
}
