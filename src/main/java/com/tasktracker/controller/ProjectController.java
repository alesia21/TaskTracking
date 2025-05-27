package com.tasktracker.controller;

import com.tasktracker.entity.Project;
import com.tasktracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")

public class ProjectController {

    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    // POST /api/projects - Create a new project

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/user/{userId}")
    public List<Project> getProjectsByUser(@PathVariable Long userId) {
        return projectService.getProjectsByUserId(userId);
    }


    @PostMapping
    public Project createProject(
            @RequestParam Long userId,
            @Valid @RequestBody Project project
    ) {
        return projectService.createProject(project, userId);
    }


    @GetMapping

    //with this values we get only the first page and 10 project in it.
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<Project> projects = projectService.getAllProjects(page, size);
        return ResponseEntity.ok(projects);
    }

    // PUT /api/projects/{id} - Update a project
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody Project project
    ) {
        Project updated = projectService.updateProject(id, project);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/projects/{id} - Delete a project
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
