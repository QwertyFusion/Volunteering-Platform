package com.example.volunteer_platform.service;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.User;
import com.example.volunteer_platform.repository.TaskRepository;
import com.example.volunteer_platform.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Create a new task associated with an organization
    public Task createTask(Long organizationId, Task task) {
        // Logic to associate the task with an organization, if necessary
        Optional<User> organization = userRepository.findById(organizationId);  // Assuming you have a UserRepository
        if (organization.isPresent()) {
            task.setOrganization(organization.get()); // Set the actual User (organization) entity, not just the ID
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Organization not found for ID: " + organizationId);
        }
    }


    // Update an existing task
    public Optional<Task> updateTask(Long taskId, Task taskDetails) {
        return taskRepository.findById(taskId)
                .map(existingTask -> {
                    existingTask.setTitle(taskDetails.getTitle());
                    existingTask.setLocation(taskDetails.getLocation());
                    existingTask.setDescription(taskDetails.getDescription());
                    existingTask.setStartDateTime(taskDetails.getStartDateTime());
                    existingTask.setEndDateTime(taskDetails.getEndDateTime());
                    existingTask.setStatus(taskDetails.getStatus());
                    existingTask.setMaxVolunteers(taskDetails.getMaxVolunteers());
                    return taskRepository.save(existingTask);
                });
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

    // Search tasks by title, location, or description
    public List<Task> searchTasks(String title, String location, String description) {
        if (title != null && !title.isEmpty()) {
            return taskRepository.findByTitleContainingIgnoreCase(title);
        } else if (location != null && !location.isEmpty()) {
            return taskRepository.findByLocationContainingIgnoreCase(location);
        } else if (description != null && !description.isEmpty()) {
            return taskRepository.findByDescriptionContainingIgnoreCase(description);
        } else {
            return taskRepository.findAll();
        }
    }

    // Get tasks by status
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(Task.TaskStatus.valueOf(status));
    }

    // Get tasks by upcoming deadlines
    public List<Task> getTasksByUpcomingDeadlines() {
        return taskRepository.findByEndDateTimeAfterOrderByEndDateTimeAsc(java.time.LocalDateTime.now());
    }
}
