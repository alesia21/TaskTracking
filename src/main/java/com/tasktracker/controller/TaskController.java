// com/tasktracker/controller/TaskController.java
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
import org.springframework.data.domain.*;
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

    // POST /api/projects/{projectId}/tasks
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Task> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody Task task
    ) {
        Optional<Project> proj = projectRepository.findById(projectId);
        if (proj.isEmpty() || task.getAssignee() == null || task.getAssignee().getId() == null)
            return ResponseEntity.badRequest().build();

        Optional<User> user = userRepository.findById(task.getAssignee().getId());
        if (user.isEmpty())
            return ResponseEntity.badRequest().build();

        task.setProject(proj.get());
        task.setAssignee(user.get());
        Task saved = taskService.createTask(task);
        return ResponseEntity.ok(saved);
    }

    // GET /api/tasks/{id}
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/projects/{projectId}/tasks?status=&page=&size=
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Task> tasks = taskService
                .getTasksByProject(projectId, status, pageable)
                .getContent();
        return ResponseEntity.ok(tasks);
    }

    // PUT /api/tasks/{id}
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody Task details
    ) {
        Optional<Task> existingOpt = taskService.getTaskById(id);
        if (existingOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Task existing = existingOpt.get();
        existing.setTitle(details.getTitle());
        existing.setDescription(details.getDescription());
        existing.setStatus(details.getStatus());
        existing.setPriority(details.getPriority());
        existing.setDueDate(details.getDueDate());

        if (details.getAssignee() != null && details.getAssignee().getId() != null) {
            Optional<User> u = userRepository.findById(details.getAssignee().getId());
            if (u.isEmpty())
                return ResponseEntity.badRequest().build();
            existing.setAssignee(u.get());
        }

        Task updated = taskService.updateTask(existing);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/tasks/{id}
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.getTaskById(id).isEmpty())
            return ResponseEntity.notFound().build();
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/tasks/due-today
    @GetMapping("/tasks/due-today")
    public ResponseEntity<List<Task>> getTasksDueToday() {
        return ResponseEntity.ok(taskService.getTasksDueToday());
    }

    // GET /api/users/{userId}/tasks
    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        if (userRepository.findById(userId).isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    // com/tasktracker/controller/TaskController.java
    @GetMapping("/users/{userId}/tasks/filter")
    public ResponseEntity<List<Task>> filterTasks(
            @PathVariable Long userId,
            @RequestParam TaskStatus status,
            @RequestParam TaskPriority priority
    ) {
        if (userRepository.findById(userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Task> list = taskService.getFilteredTasks(userId, status, priority);
        return ResponseEntity.ok(list);
    }

}
