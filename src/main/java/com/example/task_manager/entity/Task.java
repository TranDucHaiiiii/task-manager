package com.example.task_manager.entity;

import com.example.task_manager.enums.TaskStatus;

import java.time.LocalDate;

public class Task {
    private Long id;
    private String title;
    private TaskStatus status;
    private LocalDate deadline;
    private User user;
    private Project project;

    public Task(Long id, String title, LocalDate deadline, Project project) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title không được rỗng");
        }
        if (deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline phải lớn hơn hiện tại");
        }

        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.project = project;
        this.status = TaskStatus.TODO;
    }

    public void assignUser(User user) {
        if (!project.hasUser(user)) {
            throw new RuntimeException("User không thuộc project");
        }
        this.user = user;
    }

    public void updateStatus(TaskStatus status) {
        if (this.status == TaskStatus.DONE) {
            throw new RuntimeException("Task đã DONE không update");
        }
        this.status = status;
    }

    public Long getId() {
        return id;
    }
}
