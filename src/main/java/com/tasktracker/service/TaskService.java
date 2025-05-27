package com.tasktracker.service;

import com.tasktracker.entity.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(Long projectId,Task task);
    List<Task> getAllTasks(int page, int size); // updated
    Optional<Task> getTaskById(Long id);
    Task updateTask(Long id, Task taskDetails);
    List<Task> getTasksByProject(Long projectId, int page, int size);
    List<Task> getTasksByProjectAndStatus(Long projectId, String status, int page, int size);
    void deleteTask(Long id);
}