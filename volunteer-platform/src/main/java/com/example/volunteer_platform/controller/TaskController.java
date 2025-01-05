package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.dto.SkillDto;
import com.example.volunteer_platform.dto.TaskDto;
import com.example.volunteer_platform.dto.TaskPartialDto;
import com.example.volunteer_platform.model.*;
import com.example.volunteer_platform.service.SkillService;
import com.example.volunteer_platform.service.TaskSignupService;
import com.example.volunteer_platform.service.UserService;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserService userService;

    @Autowired
    private TaskSignupService taskSignupService;

    // GET all tasks posted by any organization
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // GET task by task id
    @GetMapping("/id/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.findById(taskId).orElse(null);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET or Search tasks by name, location, or description
//    @GetMapping("/search")
//    public ResponseEntity<List<Task>> searchTasks(
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) String location,
//            @RequestParam(required = false) String description) {
//
//        List<Task> tasks = taskService.searchTasks(title, location, description);
//
//        if (tasks.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//    }


    // Delete a task (Admin only)
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        Optional<Task> task = taskService.findById(taskId);
        if (task.isEmpty()) {
            return new ResponseEntity<>("No task exists with the given ID", HttpStatus.NOT_FOUND);
        }

        // Retrieve all signups for the task
        List<TaskSignup> signups = taskSignupService.getTaskSignups(taskId);

        // Delete all signups associated with the task
        for (TaskSignup signup : signups) {
            taskSignupService.deleteById(signup.getSignupId());
        }

        taskService.deleteByTaskId(taskId);
        return new ResponseEntity<>("Task deleted successfully.", HttpStatus.NO_CONTENT);
    }

    // Add Skill to Task
    @PostMapping("/{taskId}/skills")
    public ResponseEntity<Task> addSkillToTask(@PathVariable Long taskId, @RequestBody SkillDto skillDto) {
        Optional<Task> taskOpt = taskService.findById(taskId);
        if (taskOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOpt.get();
        Skill skill = skillService.findByName(skillDto.getName().toLowerCase()).orElse(null);

        if (skill == null) {
            // Create new skill if it doesn't exist
            skill = new Skill();
            skill.setName(skillDto.getName().toLowerCase());
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