package com.tasktracker.service.Impl;

import com.tasktracker.dto.ProjectCreateRequest;
import com.tasktracker.entity.Project;
import com.tasktracker.entity.User;
import com.tasktracker.repository.ProjectRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public Project createProject(ProjectCreateRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(owner);

        return projectRepository.save(project);
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    @Override
    public List<Project> getAllProjects(int page, int size) {
        return projectRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public Project updateProject(Long id, ProjectCreateRequest request) {
        Project project = getProjectById(id);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
