package com.example.task_manager.controller;

import com.example.task_manager.dto.ApiResponse;
import com.example.task_manager.dto.AuthResponse;
import com.example.task_manager.dto.LoginRequest;
import com.example.task_manager.dto.RegisterRequest;
import com.example.task_manager.dto.UserResponse;
import com.example.task_manager.entity.User;
import com.example.task_manager.service.AuthService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Auth", description = "Authentication APIs")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Create a new user with default role USER")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        UserResponse response = new UserResponse(user.getId(), user.getUsername(), roles);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Validate credentials and return JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }
}
