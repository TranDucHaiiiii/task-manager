package com.example.task_manager.controller;

import com.example.task_manager.dto.ApiResponse;
import com.example.task_manager.dto.ProjectRequest;
import com.example.task_manager.entity.Project;
import com.example.task_manager.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<Project>> create(@Valid @RequestBody ProjectRequest request) {
        Project project = projectService.create(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", project));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Project>>> getAll() {
        List<Project> projects = projectService.getAll();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", projects));
    }
}
