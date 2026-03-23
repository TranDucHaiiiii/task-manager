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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Project project;
    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Project A");
        project.setUsers(new ArrayList<>());

        user = new User();
        user.setId(2L);
        user.setUsername("user1");

        task = new Task();
        task.setId(10L);
        task.setTitle("Task 1");
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
    }

    @Test
    @DisplayName("createTask should save task when project exists")
    void createTask_shouldSaveTask_whenProjectExists() {
        TaskRequest request = new TaskRequest();
        request.setName("New Task");
        request.setDescription("Desc");
        request.setDeadline(LocalDate.now().plusDays(2));
        request.setProjectId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task saved = taskService.createTask(request);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        verify(projectRepository).findById(1L);
        Task captured = captor.getValue();

        assertEquals("New Task", captured.getTitle());
        assertEquals("Desc", captured.getDescription());
        assertEquals(project, captured.getProject());
        assertEquals(TaskStatus.TODO, captured.getStatus());
        assertEquals(request.getDeadline(), captured.getDeadline());
        assertNotNull(saved);
    }

    @Test
    @DisplayName("createTask should throw NotFoundException when project not found")
    void createTask_shouldThrow_whenProjectNotFound() {
        TaskRequest request = new TaskRequest();
        request.setName("New Task");
        request.setDeadline(LocalDate.now().plusDays(2));
        request.setProjectId(99L);

        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> taskService.createTask(request));
        assertEquals("Project not found", ex.getMessage());
        verify(projectRepository).findById(99L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("assignTask should assign user when user belongs to project")
    void assignTask_shouldAssignUser_whenUserBelongsToProject() {
        project.getUsers().add(user);

        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task saved = taskService.assignTask(10L, 2L);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        verify(taskRepository).findById(10L);
        verify(userRepository).findById(2L);
        assertEquals(user, captor.getValue().getUser());
        assertEquals(user, saved.getUser());
    }

    @Test
    @DisplayName("assignTask should throw BadRequestException when user not in project")
    void assignTask_shouldThrow_whenUserNotInProject() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> taskService.assignTask(10L, 2L));
        assertEquals("User does not belong to project's members", ex.getMessage());
        verify(taskRepository).findById(10L);
        verify(userRepository).findById(2L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("assignTask should throw NotFoundException when task not found")
    void assignTask_shouldThrow_whenTaskNotFound() {
        when(taskRepository.findById(10L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> taskService.assignTask(10L, 2L));
        assertEquals("Task not found", ex.getMessage());
        verify(taskRepository).findById(10L);
        verify(userRepository, never()).findById(anyLong());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("updateStatus should update to IN_PROGRESS when task is TODO")
    void updateStatus_shouldUpdate_whenTaskIsTodo() {
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updated = taskService.updateStatus(10L, TaskStatus.IN_PROGRESS);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        verify(taskRepository).findById(10L);
        assertEquals(TaskStatus.IN_PROGRESS, captor.getValue().getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, updated.getStatus());
    }

    @Test
    @DisplayName("updateStatus should throw BadRequestException when task is DONE")
    void updateStatus_shouldThrow_whenTaskIsDone() {
        task.setStatus(TaskStatus.DONE);
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> taskService.updateStatus(10L, TaskStatus.IN_PROGRESS));
        assertEquals("Task is DONE and cannot be updated", ex.getMessage());
        verify(taskRepository).findById(10L);
        verify(taskRepository, never()).save(any(Task.class));
    }
}
