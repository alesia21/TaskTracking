package com.tasktracker.service;

import com.tasktracker.entity.Project;

import java.util.List;
public interface ProjectService {
    Project createProject(Project project, Long userId);
    List<Project> getProjectsByUserId(Long userId);
    Project getProjectById(Long id);

    List<Project> getAllProjects(int page, int size);
    Project updateProject(Long id, Project updatedProject);
    void deleteProject(Long id);
}

