package com.tasktracker.service;

import com.tasktracker.entity.Task;
import com.tasktracker.enums.TaskPriority;
import com.tasktracker.enums.TaskStatus;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.repository.ProjectRepository;
import com.tasktracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new Task. Assumes controller has already set project and assignee.
     */
    public Task createTask(Task task) {
        // save and return
        return taskRepository.save(task);
    }

    /**
     * Retrieves a task by its ID.
     */
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Lists tasks by project, optionally filtering by status, using pagination.
     */
    public Page<Task> getTasksByProject(Long projectId, TaskStatus status, Pageable pageable) {
        // If status filter is provided
        if (status != null) {
            return taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        }
        // Otherwise list all
        return taskRepository.findByProjectId(projectId, pageable);
    }

    /**
     * Updates an existing task. Assumes controller has copied the up-to-date fields.
     */
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID.
     */
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Returns all tasks whose dueDate is today.
     */
    public List<Task> getTasksDueToday() {
        return taskRepository.findByDueDate(LocalDate.now());
    }

    /**
     * Returns all tasks assigned to the given user.
     */
    public List<Task> getTasksByUser(Long userId) {
        // ensure user exists
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }
        return taskRepository.findByAssignee_Id(userId);
    }

    /**
     * Returns tasks assigned to userId, filtered by status and priority.
     */
    public List<Task> getFilteredTasks(Long userId, TaskStatus status, TaskPriority priority) {
        // ensure user exists
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }
        return taskRepository.findByAssignee_IdAndStatusAndPriority(userId, status, priority);
    }
}
