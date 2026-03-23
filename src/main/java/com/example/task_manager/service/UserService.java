package com.example.task_manager.service;

import com.example.task_manager.entity.User;
import com.example.task_manager.exception.AppException;
import com.example.task_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new AppException("Username không được để trống");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AppException("Username đã tồn tại");
        }
        return userRepository.save(user);
    }
}
