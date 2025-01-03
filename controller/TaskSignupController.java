package com.example.volunteer_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.service.TaskSignupService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/signup")
public class TaskSignupController {

    @Autowired
    private TaskSignupService taskSignupService;

    // Sign up for a task
    @PostMapping("/{taskId}/user/{userId}")
    public ResponseEntity<?> signUpForTask(@PathVariable Long taskId, @PathVariable Long userId) {
        Optional<TaskSignup> taskSignup = taskSignupService.signUpForTask(taskId, userId);

        if (taskSignup.isEmpty()) {
            return ResponseEntity.badRequest().body("Task or User not found.");
        }
        return ResponseEntity.ok(taskSignup.get());
    }

    // Get all signups for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserSignups(@PathVariable Long userId) {
        List<TaskSignup> signups = taskSignupService.getUserSignups(userId);
        
        if (signups.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(signups);
    }

    // Cancel a task signup
 // Cancel a task signup
    @PutMapping("/cancel/{signupId}")
    public ResponseEntity<?> cancelSignup(@PathVariable Long signupId) {
        Optional<TaskSignup> signupOptional = taskSignupService.cancelSignup(signupId);

        if (signupOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Signup not found.");
        }

        TaskSignup signup = signupOptional.get();
        
        // Checking if the status allows cancellation (only "UPCOMING" signups can be cancelled)
        if (signup.getStatus() != TaskSignup.SignupStatus.UPCOMING) {
            return ResponseEntity.badRequest().body("Only upcoming tasks can be cancelled.");
        }

        // Save the cancellation status
        signup.setStatus(TaskSignup.SignupStatus.CANCELLED);
        taskSignupService.save(signup); // Assuming this is part of the service

        return ResponseEntity.ok(signup); // Return the updated signup object
    }

}
