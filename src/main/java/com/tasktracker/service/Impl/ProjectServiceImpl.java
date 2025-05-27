package com.tasktracker.service.Impl;

import com.tasktracker.entity.Project;
import com.tasktracker.entity.User;
import com.tasktracker.repository.ProjectRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));
    }

    @Override
    public Project createProject(Project project, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        project.setOwner(user);
        return projectRepository.save(project);
    }


    @Override
    public List<Project> getProjectsByUserId(Long userId) {
        return projectRepository.findByOwnerId(userId);
    }




    @Override
    public List<Project> getAllProjects(int page, int size) {
        return projectRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public Project updateProject(Long id, Project updatedProject) {
        Project existing = getProjectById(id);

        existing.setName(updatedProject.getName());
        existing.setDescription(updatedProject.getDescription());

        // opsionale: nëse do të lejojmë përditësim të owner
        if (updatedProject.getOwner() != null) {
            existing.setOwner(updatedProject.getOwner());
        }

        return projectRepository.save(existing);
    }


    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
