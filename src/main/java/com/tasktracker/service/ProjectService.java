package com.tasktracker.service;

import com.tasktracker.dto.ProjectCreateRequest;
import com.tasktracker.entity.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectCreateRequest request);
    Project getProjectById(Long id);
    List<Project> getAllProjects(int page, int size);
    Project updateProject(Long id, ProjectCreateRequest request);
    void deleteProject(Long id);
}
