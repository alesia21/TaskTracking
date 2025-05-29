package com.tasktracker.controller;

import com.tasktracker.entity.Project;
import com.tasktracker.entity.User;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;

    // Create a new project using ownerId for binding
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        Long ownerId = project.getOwnerId();
        if (ownerId == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> ownerOpt = userRepository.findById(ownerId);
        if (ownerOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        project.setOwner(ownerOpt.get());
        Project saved = projectService.saveProject(project);
        return ResponseEntity.ok(saved);
    }

    // Get project by ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all projects (flattened list)
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Project> projects = projectService
                .getAllProjects(PageRequest.of(page, size))
                .getContent();
        return ResponseEntity.ok(projects);
    }

    // Update a project by ID
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody Project details) {
        Optional<Project> existingOpt = projectService.getProjectById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Project existing = existingOpt.get();
        existing.setName(details.getName());
        existing.setDescription(details.getDescription());
        Long newOwnerId = details.getOwnerId();
        if (newOwnerId != null) {
            Optional<User> newOwnerOpt = userRepository.findById(newOwnerId);
            if (newOwnerOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            existing.setOwner(newOwnerOpt.get());
        }
        Project updated = projectService.saveProject(existing);
        return ResponseEntity.ok(updated);
    }

    // Delete a project by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (projectService.getProjectById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
