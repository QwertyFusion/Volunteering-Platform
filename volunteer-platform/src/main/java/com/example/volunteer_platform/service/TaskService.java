package com.example.volunteer_platform.service;

import com.example.volunteer_platform.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * TaskService provides methods to manage tasks in the system.
 */
public interface TaskService {

    /**
     * Get all tasks in the system.
     *
     * @return List of tasks.
     */
    List<Task> getAllTasks();

    /**
     * Create a new task associated with an organization.
     *
     * @param task Task to be saved.
     */
    void saveTask(Task task);

    /**
     * Find a task by its ID.
     *
     * @param taskId Task ID.
     * @return Optional containing the task if found.
     */
    Optional<Task> findById(Long taskId);

    /**
     * Delete a task by its ID.
     *
     * @param taskId Task ID.
     */
    void deleteByTaskId(Long taskId);

    /**
     * Search tasks by title, location, or description.
     *
     * @param title Title of the task.
     * @param location Location of the task.
     * @param description Description of the task.
     * @return List of tasks matching the search criteria.
     */
    List<Task> searchTasks(String title, String location, String description);
    
   
}