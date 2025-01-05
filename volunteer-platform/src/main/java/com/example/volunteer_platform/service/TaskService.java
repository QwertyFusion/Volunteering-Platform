package com.example.volunteer_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * TaskService provides methods to manage tasks in the system.
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Get all tasks in the system.
     *
     * @return List of tasks.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Create a new task associated with an organization.
     *
     * @param task Task to be saved.
     */
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    /**
     * Find a task by its ID.
     *
     * @param taskId Task ID.
     * @return Optional containing the task if found.
     */
    public Optional<Task> findById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    /**
     * Delete a task by its ID.
     *
     * @param taskId Task ID.
     */
    public void deleteByTaskId(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    /**
     * Search tasks by title, location, or description.
     *
     * @param title Title of the task.
     * @param location Location of the task.
     * @param description Description of the task.
     * @return List of tasks matching the search criteria.
     */
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