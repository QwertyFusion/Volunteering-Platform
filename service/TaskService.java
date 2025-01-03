package com.example.volunteer_platform.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Create a new task associated with an organization
    public Task createTask(Long organizationId, Task task) {
        // Assuming the task repository has the required logic to save tasks
        return taskRepository.save(task);
    }

    // Update an existing task
    public Task updateTask(Long taskId, Task taskDetails) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setLocation(taskDetails.getLocation());
                    task.setDescription(taskDetails.getDescription());
                    task.setDate(taskDetails.getDate());
                    return taskRepository.save(task);
                }).orElse(null);
    }

    // Delete a task
    public boolean deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get tasks by organization
    public List<Task> getTasksByOrganization(Long organizationId) {
        return taskRepository.findByOrganizationId(organizationId);
    }

    // Search tasks by name, location, or description
    public List<Task> searchTasks(String title, String location, String description) {
       
         if (title != null) {
            return taskRepository.findByTitleContaining(title);
        } else if (location != null) {
            return taskRepository.findByLocationContaining(location);
        } else if (description != null) {
            return taskRepository.findByDescriptionContaining(description);
        } else {
            return taskRepository.findAll();
        }
    }

}