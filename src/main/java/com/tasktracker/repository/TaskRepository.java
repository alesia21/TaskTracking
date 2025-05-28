// src/main/java/com/tasktracker/repository/TaskRepository.java
package com.tasktracker.repository;

import com.tasktracker.entity.Task;
import com.tasktracker.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);

    List<Task> findByProjectId(Long projectId);
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
    @Query("SELECT t FROM Task t WHERE t.status = :status")
    List<Task> findAllByStatusJPQL(@Param("status") TaskStatus status);
    List<Task> findByDueDate(LocalDate dueDate);
    List<Task> findByAssigneeId(Long userId);
}
