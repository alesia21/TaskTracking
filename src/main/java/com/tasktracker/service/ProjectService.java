package com.tasktracker.service;

import com.tasktracker.entity.Project;
import com.tasktracker.entity.User;
import com.tasktracker.repository.ProjectRepository;
import com.tasktracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
//Service layer for Project operations.

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public Project createProject(Project project) {
        Long ownerId = project.getOwnerId();
        if (ownerId == null) {
            throw new IllegalArgumentException("OwnerId is required");
        }
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("No User with id=" + ownerId));
        project.setOwner(owner);
        project.setCreatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }


    public Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
    }


    public List<Project> listProjects(int page, int size) {
        return projectRepository.findAll(PageRequest.of(page, size))
                .getContent();
    }


    public Project updateProject(Long id, Project updates) {
        Project existing = getProject(id);

        existing.setName(updates.getName());
        existing.setDescription(updates.getDescription());

        Long newOwnerId = updates.getOwnerId();
        if (newOwnerId != null && !newOwnerId.equals(existing.getOwner().getId())) {
            User newOwner = userRepository.findById(newOwnerId)
                    .orElseThrow(() -> new IllegalArgumentException("No User with id=" + newOwnerId));
            existing.setOwner(newOwner);
        }

        return projectRepository.save(existing);
    }


    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found: " + id);
        }
        projectRepository.deleteById(id);
    }
}
