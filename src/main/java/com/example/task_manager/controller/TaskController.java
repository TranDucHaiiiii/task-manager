package com.example.task_manager.controller;

import com.example.task_manager.dto.ApiResponse;
import com.example.task_manager.dto.TaskRequest;
import com.example.task_manager.dto.UpdateTaskStatusRequest;
import com.example.task_manager.entity.Task;
import com.example.task_manager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> create(@Valid @RequestBody TaskRequest request) {
        Task task = taskService.createTask(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", task));
    }

    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<ApiResponse<Task>> assign(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", task));
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<Task>> updateStatus(@PathVariable Long taskId,
                                                          @Valid @RequestBody UpdateTaskStatusRequest request) {
        Task task = taskService.updateStatus(taskId, request.getStatus());
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", task));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Task>>> getTasksByUser(@PathVariable Long userId) {
        List<Task> tasks = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", tasks));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<Task>>> getTasksByProject(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", tasks));
    }
}
