package com.example.volunteer_platform.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.repository.TaskRepository;
import com.example.volunteer_platform.service.TaskService;

import java.util.List;
import java.util.Optional;

/**
 * TaskServiceImplementation provides methods to manage tasks in the system.
 * This is an implementation of the TaskService interface.
 */
@Service
public class TaskServiceImplementation implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
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