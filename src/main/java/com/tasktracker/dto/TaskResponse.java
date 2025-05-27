// src/main/java/com/tasktracker/dto/TaskResponse.java
package com.tasktracker.dto;

import com.tasktracker.entity.enums.TaskPriority;
import com.tasktracker.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;

    private Long projectId;
    private String projectName;

    private Long assigneeId;
    private String assigneeUsername;
}
