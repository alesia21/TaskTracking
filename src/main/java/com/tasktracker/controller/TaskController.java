// src/main/java/com/tasktracker/controller/TaskController.java
package com.tasktracker.controller;

import com.tasktracker.entity.Task;
import com.tasktracker.entity.Project;
import com.tasktracker.entity.User;
import com.tasktracker.enums.TaskPriority;
import com.tasktracker.enums.TaskStatus;
import com.tasktracker.repository.ProjectRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    /** POST /api/projects/{projectId}/tasks */
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Task> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody Task task
    ) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isEmpty() || task.getAssignee() == null || task.getAssignee().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> assigneeOpt = userRepository.findById(task.getAssignee().getId());
        if (assigneeOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        task.setProject(projectOpt.get());
        task.setAssignee(assigneeOpt.get());
        Task saved = taskService.createTask(task);
        return ResponseEntity.ok(saved);
    }

    /** GET /api/tasks/{id} */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> taskOpt = taskService.getTaskById(id);
        return taskOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET /api/projects/{projectId}/tasks?status=&page=&size= */
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Task> pageResult = taskService.getTasksByProject(projectId, status, pageable);
        return ResponseEntity.ok(pageResult.getContent());
    }

    /** PUT /api/tasks/{id} */
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody Task details
    ) {
        Optional<Task> existingOpt = taskService.getTaskById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task existing = existingOpt.get();
        existing.setTitle(details.getTitle());
        existing.setDescription(details.getDescription());
        existing.setStatus(details.getStatus());
        existing.setPriority(details.getPriority());
        existing.setDueDate(details.getDueDate());
        if (details.getAssignee() != null && details.getAssignee().getId() != null) {
            Optional<User> userOpt = userRepository.findById(details.getAssignee().getId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            existing.setAssignee(userOpt.get());
        }
        Task updated = taskService.updateTask(existing);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/tasks/{id} */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<Task> existing = taskService.getTaskById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /** GET /api/tasks/due-today */
    @GetMapping("/tasks/due-today")
    public ResponseEntity<List<Task>> getTasksDueToday() {
        List<Task> list = taskService.getTasksDueToday();
        return ResponseEntity.ok(list);
    }

    /** GET /api/users/{userId}/tasks */
    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Task> list = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(list);
    }

    /** GET /api/users/{userId}/tasks/filter?status=&priority= */
    @GetMapping("/users/{userId}/tasks/filter")
    public ResponseEntity<List<Task>> filterTasks(
            @PathVariable Long userId,
            @RequestParam TaskStatus status,
            @RequestParam TaskPriority priority
    ) {
        if (userRepository.findById(userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Task> filtered = taskService.getFilteredTasks(userId, status, priority);
        return ResponseEntity.ok(filtered);
    }
}
