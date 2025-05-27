package com.tasktracker.controller;

import com.tasktracker.dto.ProjectCreateRequest;
import com.tasktracker.entity.Project;
import com.tasktracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // ✅ POST /api/projects
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    // ✅ GET /api/projects/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // ✅ GET /api/projects?page=0&size=10
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }

    // ✅ PUT /api/projects/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectCreateRequest request
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    // ✅ DELETE /api/projects/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
