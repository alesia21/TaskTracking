package com.tasktracker.repository;

import com.tasktracker.entity.Task;

import com.tasktracker.enums.TaskStatus;
import com.tasktracker.enums.TaskPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // paging methods for project
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);

    // due-today
    List<Task> findByDueDate(LocalDate dueDate);

    // these are the new ones you need:
    // fetch all tasks assigned to a given user ID
    List<Task> findByAssignee_Id(Long userId);

    // fetch all tasks for a user filtered by status and priority
    List<Task> findByAssignee_IdAndStatusAndPriority(
            Long userId,
            TaskStatus status,
            TaskPriority priority
    );
}
