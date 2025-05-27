package com.tasktracker.controller;

import com.tasktracker.entity.Task;
import com.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // ✅ POST /api/projects/{projectId}/tasks
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<Task> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody Task task
    ) {
        Task created = taskService.createTask(projectId, task);
        return ResponseEntity.ok(created);
    }

    // ✅ GET /api/projects/{projectId}/tasks?status=TODO&page=0&size=5
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<Task> tasks;
        if (status != null) {
            tasks = taskService.getTasksByProjectAndStatus(projectId, status.toUpperCase(), page, size);
        } else {
            tasks = taskService.getTasksByProject(projectId, page, size);
        }

        return ResponseEntity.ok(tasks);
    }

    // ✅ GET /api/tasks/{id}
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ GET /api/tasks (global paginated)
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Task> tasks = taskService.getAllTasks(page, size);
        return ResponseEntity.ok(tasks);
    }

    // ✅ PUT /api/tasks/{id}
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        try {
            Task updatedTask = taskService.updateTask(id, taskDetails);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE /api/tasks/{id}
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
