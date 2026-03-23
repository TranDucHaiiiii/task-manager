package com.example.task_manager.entity;

import com.example.task_manager.exception.AppException;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private Long id;
    private String name;
    private String description;
    private List<User> users = new ArrayList<>();

    public Project(Long id, String name, String description) {
        if (name == null || name.isBlank()) {
            throw new AppException("Tên project không được rỗng");
        }
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean hasUser(User user) {
        return users.contains(user);
    }

    public Long getId() {
        return id;
    }
}

