package com.example.task_manager.controller;

import com.example.task_manager.dto.ApiResponse;
import com.example.task_manager.dto.TaskRequest;
import com.example.task_manager.dto.UpdateTaskStatusRequest;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import org.springframework.security.access.AccessDeniedException;
import com.example.task_manager.service.TaskService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task", description = "Task management APIs")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Create a task in a project with status TODO")
    public ResponseEntity<ApiResponse<Task>> create(@Valid @RequestBody TaskRequest request) {
        Task task = taskService.createTask(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", task));
    }

    @PutMapping("/{taskId}/assign/{userId}")
    @Operation(summary = "Assign task", description = "Assign a task to a user in the same project")
    public ResponseEntity<ApiResponse<Task>> assign(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", task));
    }

    @PutMapping("/{taskId}/status")
    @Operation(summary = "Update task status", description = "Update task status with business rules applied")
    public ResponseEntity<ApiResponse<Task>> updateStatus(@PathVariable Long taskId,
                                                          @Valid @RequestBody UpdateTaskStatusRequest request) {
        Task task = taskService.updateStatus(taskId, request.getStatus());
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", task));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List tasks by user", description = "USER can only view own tasks, MANAGER can view any")
    public ResponseEntity<ApiResponse<List<Task>>> getTasksByUser(@PathVariable Long userId,
                                                                  Authentication authentication) {
        User currentUser = taskService.getUserByUsername(authentication.getName());
        if (!isManager(authentication) && !currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }
        List<Task> tasks = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", tasks));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "List tasks by project", description = "USER only sees own tasks in project")
    public ResponseEntity<ApiResponse<List<Task>>> getTasksByProject(@PathVariable Long projectId,
                                                                     Authentication authentication) {
        User currentUser = taskService.getUserByUsername(authentication.getName());
        List<Task> tasks;
        if (isManager(authentication)) {
            tasks = taskService.getTasksByProject(projectId);
        } else {
            tasks = taskService.getTasksByProjectForUser(projectId, currentUser.getId());
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", tasks));
    }

    private boolean isManager(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MANAGER"));
    }
}
