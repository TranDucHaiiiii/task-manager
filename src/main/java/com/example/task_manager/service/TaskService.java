package com.example.task_manager.service;

import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.exception.AppException;
import com.example.task_manager.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        if (task == null) {
            throw new AppException("Task null");
        }
        tasks.add(task);
    }

    public void deleteTask(Long id) {
        tasks.removeIf(t -> t.getId().equals(id));
    }

    public Task findById(Long id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AppException("Không tìm thấy task"));
    }

    public void updateStatus(Long id, TaskStatus status) {
        Task task = findById(id);
        task.updateStatus(status);
    }

    public void assignTask(Long taskId, User user) {
        Task task = findById(taskId);
        task.assignUser(user);
    }
}
