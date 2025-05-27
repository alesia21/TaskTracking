package com.tasktracker.repository;

import com.tasktracker.entity.Task;
import com.tasktracker.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByProjectId(Long projectId);

    List<Task> findByProjectIdAndStatus(Long projectId, Status status);

    List<Task> findByAssigneeId(Long userId);
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByProjectIdAndStatus(Long projectId, Status status, Pageable pageable);

    List<Task> findByDueDate(LocalDate dueDate);

    @Query("SELECT t FROM Task t WHERE t.status = 'TODO' AND t.dueDate = CURRENT_DATE")
    List<Task> findTasksDueToday();
}
