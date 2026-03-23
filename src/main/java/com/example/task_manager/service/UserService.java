package com.example.task_manager.service;

import com.example.task_manager.entity.User;
import com.example.task_manager.exception.BadRequestException;
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
            throw new BadRequestException("Username khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username Ä‘Ã£ tá»“n táº¡i");
        }
        return userRepository.save(user);
    }
}
