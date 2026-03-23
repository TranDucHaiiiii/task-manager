package com.example.task_manager.service;

import com.example.task_manager.dto.TaskRequest;
import com.example.task_manager.entity.Project;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.enums.TaskStatus;
import com.example.task_manager.exception.BadRequestException;
import com.example.task_manager.exception.NotFoundException;
import com.example.task_manager.repository.ProjectRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Transactional
    public Task createTask(TaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));

        if (request.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
            // Business rule: deadline must be greater than current date
            throw new BadRequestException("Deadline must be greater than current date");
        }

        Task task = new Task();
        task.setTitle(request.getName());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
        return taskRepository.save(task);
    }

    @Transactional
    public Task assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Project project = task.getProject();
        if (project == null) {
            throw new BadRequestException("Task has no project");
        }

        // Business rule: only members of the project can be assigned
        boolean belongsToProject = project.getUsers()
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()));
        if (!belongsToProject) {
            throw new BadRequestException("User does not belong to project's members");
        }

        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        // Business rule: do not allow updates when DONE
        if (task.getStatus() == TaskStatus.DONE) {
            throw new BadRequestException("Task is DONE and cannot be updated");
        }

        // Business rule: optional - do not allow TODO -> DONE directly
        if (task.getStatus() == TaskStatus.TODO && newStatus == TaskStatus.DONE) {
            throw new BadRequestException("Cannot move directly from TODO to DONE");
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }
}
