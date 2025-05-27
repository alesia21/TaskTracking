// src/main/java/com/tasktracker/dto/TaskRequest.java
package com.tasktracker.dto;

import com.tasktracker.entity.enums.TaskPriority;
import com.tasktracker.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    private String description;

    @NotNull
    private TaskStatus status;

    @NotNull
    private TaskPriority priority;

    private LocalDate dueDate;

    // opsionale: ID i përdoruesit-assignee
    private Long assigneeId;
}
