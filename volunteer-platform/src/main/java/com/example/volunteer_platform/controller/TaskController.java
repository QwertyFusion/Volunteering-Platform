package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.dto.SkillDto;
import com.example.volunteer_platform.model.Skill;
import com.example.volunteer_platform.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.service.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SkillService skillService;

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

    // Add Skill to Task
    @PostMapping("/{taskId}/skills")
    public ResponseEntity<Task> addSkillToTask(@PathVariable Long taskId, @RequestBody SkillDto skillDto) {
        Optional<Task> taskOpt = taskService.findById(taskId);
        if (taskOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOpt.get();
        Skill skill = skillService.findByName(skillDto.getName()).orElse(null);

        if (skill == null) {
            // Create new skill if it doesn't exist
            skill = new Skill();
            skill.setName(skillDto.getName());
            skillService.saveSkill(skill);
        }

        // Add skill to task
        task.getSkills().add(skill);
        taskService.saveTask(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // Remove Skill from Task
    @DeleteMapping("/{taskId}/skills/{skillId}")
    public ResponseEntity<Task> removeSkillFromTask(@PathVariable Long taskId, @PathVariable Long skillId) {
        Optional<Task> taskOpt = taskService.findById(taskId);
        if (taskOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOpt.get();
        Skill skill = skillService.findById(skillId).orElse(null);

        if (skill == null || !task.getSkills().contains(skill)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Remove skill from task
        task.getSkills().remove(skill);
        taskService.saveTask(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}