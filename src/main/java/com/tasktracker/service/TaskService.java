// com/tasktracker/service/TaskService.java
package com.tasktracker.service;

import com.tasktracker.entity.Task;
import com.tasktracker.enums.TaskPriority;
import com.tasktracker.enums.TaskStatus;
import com.tasktracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Page<Task> getTasksByProject(Long projectId, TaskStatus status, Pageable pageable) {
        if (status != null) {
            return taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        }
        return taskRepository.findByProjectId(projectId, pageable);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksDueToday() {
        return taskRepository.findByDueDate(LocalDate.now());
    }

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAssigneeId(userId);
    }

    // com/tasktracker/service/TaskService.java
    public List<Task> getFilteredTasks(Long userId, TaskStatus status, TaskPriority priority) {
        return taskRepository.findByAssigneeAndStatusAndPriority(userId, status, priority);
    }
}


