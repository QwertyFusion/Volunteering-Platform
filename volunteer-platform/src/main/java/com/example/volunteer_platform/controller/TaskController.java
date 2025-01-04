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

    // GET all tasks posted by any organization
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // GET all tasks posted by any particular organization
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Task>> getTasksByOrganization(@PathVariable Long organizationId) {
        List<Task> tasks = taskService.getTasksByOrganization(organizationId);

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // GET or Search tasks by name, location, or description
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String description) {

        List<Task> tasks = taskService.searchTasks(title, location, description);

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Add skill by name to task

    // Remove skill by name to task

    // POST or Create a new task
    // Request includes:
    // {
    //    "title": "Community Cleanup",
    //    "description": "Join us for a community cleanup event to beautify our local park.",
    //    "location": "Central Park, Main St.",
    //    "eventDate": "2023-12-15",  // Ensure this date is in the future
    //    "organization": {
    //        "id": 1  // Assuming the organization with ID 1 exists
    //    },
    //    "skills": [
    //        {
    //            "id": 1  // Assuming the skill with ID 1 exists
    //        },
    //        {
    //            "id": 2  // Assuming the skill with ID 2 exists
    //        }
    //    ]
    // }
    @PostMapping()
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        try {
            taskService.saveTask(task);
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT or Update an existing task
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




}