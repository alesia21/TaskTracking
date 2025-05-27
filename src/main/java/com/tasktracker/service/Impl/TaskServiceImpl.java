package com.tasktracker.service.Impl;

import com.tasktracker.entity.Project;
import com.tasktracker.entity.Task;
import com.tasktracker.entity.User;
import com.tasktracker.entity.enums.Status;
import com.tasktracker.repository.ProjectRepository;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public List<Task> getTasksByProject(Long projectId, int page, int size) {
        return taskRepository.findByProjectId(projectId, PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<Task> getTasksByProjectAndStatus(Long projectId, String status, int page, int size) {
        try {
            Status enumStatus = Status.valueOf(status.toUpperCase());
            return taskRepository.findByProjectIdAndStatus(projectId, enumStatus, PageRequest.of(page, size)).getContent();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }


    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task createTask(Long projectId, Task task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        task.setProject(project);

        if (task.getAssignee() != null && task.getAssignee().getId() != null) {
            User assignee = userRepository.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks(int page, int size) {
        return taskRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setProject(taskDetails.getProject());
        existingTask.setAssignee(taskDetails.getAssignee());

        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}
