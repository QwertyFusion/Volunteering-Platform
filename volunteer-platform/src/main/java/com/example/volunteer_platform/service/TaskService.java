package com.example.volunteer_platform.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Create a new task associated with an organization
    public void saveTask(Task task) {
        // Assuming the task repository has the required logic to save tasks
        taskRepository.save(task);
    }

    // Get task by id
    public Optional<Task> findById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    // Delete a task
    public void deleteByTaskId(Long taskId) {
        taskRepository.deleteById(taskId);
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