package com.example.volunteer_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Create a new task associated with an organization
    @PostMapping("/create/{organizationId}")
    public ResponseEntity<Task> createTask(@PathVariable Long organizationId, @RequestBody Task task) {
        Task createdTask = taskService.createTask(organizationId, task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Update an existing task
    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(taskId, taskDetails);
        
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete a task
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        boolean isDeleted = taskService.deleteTask(taskId);
        
        if (isDeleted) {
            return ResponseEntity.ok("Task deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(tasks);
    }

    // Get tasks by organization
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Task>> getTasksByOrganization(@PathVariable Long organizationId) {
        List<Task> tasks = taskService.getTasksByOrganization(organizationId);
        
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(tasks);
    }

    // Search tasks by name, location, or description
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String description) {

        List<Task> tasks = taskService.searchTasks(title, location, description);
        
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tasks);
    }
}