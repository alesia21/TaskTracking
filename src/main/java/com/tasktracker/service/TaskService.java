// src/main/java/com/tasktracker/service/TaskService.java
package com.tasktracker.service;

import com.tasktracker.dto.TaskRequest;
import com.tasktracker.entity.Task;
import com.tasktracker.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    Task createTask(Long projectId, TaskRequest request);
    Task getTaskById(Long id);

    Page<Task> getTasksByProject(Long projectId, Pageable pageable);
    Page<Task> getTasksByProjectAndStatus(Long projectId, TaskStatus status, Pageable pageable);

    List<Task> getTasksByProject(Long projectId);
    List<Task> getTasksByProjectAndStatus(Long projectId, TaskStatus status);

    Task updateTask(Long id, TaskRequest request);
    void deleteTask(Long id);

    List<Task> getTasksDueToday();
    List<Task> getTasksByUser(Long userId);
}
