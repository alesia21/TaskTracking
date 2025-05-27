// src/main/java/com/tasktracker/service/impl/TaskServiceImpl.java
package com.tasktracker.service.Impl;

import com.tasktracker.dto.TaskRequest;
import com.tasktracker.entity.Project;
import com.tasktracker.entity.Task;
import com.tasktracker.entity.User;
import com.tasktracker.entity.enums.TaskStatus;
import com.tasktracker.exception.ResourceNotFoundException;
import com.tasktracker.repository.ProjectRepository;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public Task createTask(Long projectId, TaskRequest req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        Task task = new Task();
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setStatus(req.getStatus());
        task.setPriority(req.getPriority());
        task.setDueDate(req.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setProject(project);

        if (req.getAssigneeId() != null) {
            User assignee = userRepository.findById(req.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", req.getAssigneeId()));
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
    }

    @Override
    public Page<Task> getTasksByProject(Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable);
    }

    @Override
    public Page<Task> getTasksByProjectAndStatus(Long projectId, TaskStatus status, Pageable pageable) {
        return taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
    }

    @Override
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public List<Task> getTasksByProjectAndStatus(Long projectId, TaskStatus status) {
        return taskRepository.findByProjectIdAndStatus(projectId, status);
    }

    @Override
    public Task updateTask(Long id, TaskRequest req) {
        Task task = getTaskById(id);
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setStatus(req.getStatus());
        task.setPriority(req.getPriority());
        task.setDueDate(req.getDueDate());

        if (req.getAssigneeId() != null) {
            User assignee = userRepository.findById(req.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", req.getAssigneeId()));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getTasksDueToday() {
        return taskRepository.findByDueDate(LocalDate.now());
    }

    @Override
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAssigneeId(userId);
    }
}
