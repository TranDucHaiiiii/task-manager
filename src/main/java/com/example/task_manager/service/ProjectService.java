package com.example.task_manager.service;

import com.example.task_manager.dto.ProjectRequest;
import com.example.task_manager.entity.Project;
import com.example.task_manager.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project create(ProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getAll() {
        return projectRepository.findAll();
    }
}
