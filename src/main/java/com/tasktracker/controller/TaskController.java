// src/main/java/com/tasktracker/controller/TaskController.java
package com.tasktracker.controller;

import com.tasktracker.dto.TaskRequest;
import com.tasktracker.dto.TaskResponse;
import com.tasktracker.entity.Task;
import com.tasktracker.entity.enums.TaskStatus;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    // 1) POST /api/projects/{projectId}/tasks
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequest req
    ) {
        Task t = taskService.createTask(projectId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(t));
    }
    @GetMapping("/tasks/status/{status}")
    public List<TaskResponse> getByStatus(@PathVariable TaskStatus status) {
        return taskRepository.findAllByStatusJPQL(status).stream()
                .map(t -> TaskResponse.builder()
                        .id(t.getId())
                        .title(t.getTitle())
                        .description(t.getDescription())
                        .status(t.getStatus())
                        .priority(t.getPriority())
                        .dueDate(t.getDueDate())
                        .createdAt(t.getCreatedAt())
                        .projectId(t.getProject().getId())
                        .projectName(t.getProject().getName())
                        .assigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null)
                        .assigneeUsername(t.getAssignee() != null ? t.getAssignee().getUsername() : null)
                        .build())
                .collect(Collectors.toList());
    }


    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(toDto(taskService.getTaskById(id)));
    }

    // 3) GET /api/projects/{projectId}/tasks?status=&page=&size=
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Page<TaskResponse>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Task> p = (status == null)
                ? taskService.getTasksByProject(projectId, pr)
                : taskService.getTasksByProjectAndStatus(projectId, status, pr);

        Page<TaskResponse> dtoPage = p.map(this::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    // 4) PUT /api/tasks/{id}
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest req
    ) {
        return ResponseEntity.ok(toDto(taskService.updateTask(id, req)));
    }

    // 5) DELETE /api/tasks/{id}
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // 6) GET /api/tasks/due-today
    @GetMapping("/tasks/due-today")
    public ResponseEntity<List<TaskResponse>> getTasksDueToday() {
        List<TaskResponse> list = taskService.getTasksDueToday()
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // 7) GET /api/users/{userId}/tasks
    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long userId) {
        List<TaskResponse> list = taskService.getTasksByUser(userId)
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Mapper helper
    private TaskResponse toDto(Task t) {
        return TaskResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus())
                .priority(t.getPriority())
                .dueDate(t.getDueDate())
                .createdAt(t.getCreatedAt())
                .projectId(t.getProject().getId())
                .projectName(t.getProject().getName())
                .assigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null)
                .assigneeUsername(t.getAssignee() != null ? t.getAssignee().getUsername() : null)
                .build();


    }
}
