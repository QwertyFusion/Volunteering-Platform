package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.volunteer_platform.dto.TaskSignupDto;
import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.service.TaskService;
import com.example.volunteer_platform.service.TaskSignupService;
import com.example.volunteer_platform.service.UserService;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * TaskSignupController handles API endpoints for managing task signups by volunteers.
 */
@RestController
@RequestMapping("/api/task-signups")
public class TaskSignupController {

    @Autowired
    private TaskSignupService taskSignupService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    /**
     * Get all volunteers signed up for tasks.
     *
     * @return List of task signups or HTTP 204 if no signups exist.
     */
    @GetMapping
    public ResponseEntity<List<TaskSignup>> getAllSignups() {
        List<TaskSignup> signups = taskSignupService.getAllSignups();

        if (signups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(signups, HttpStatus.OK);
    }

    /**
     * Get all task signups by a specific volunteer.
     *
     * @param volunteerId Volunteer ID.
     * @return List of task signups or HTTP 404 if volunteer not found.
     */
    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<TaskSignup>> getUserSignups(@PathVariable Long volunteerId) {
        Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
        if (volunteerOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<TaskSignup> signups = taskSignupService.getUserSignups(volunteerId);

        if (signups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(signups, HttpStatus.OK);
    }

    /**
     * Get all task signups for a specific task.
     *
     * @param taskId Task ID.
     * @return List of task signups or HTTP 404 if task not found.
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskSignup>> getTaskSignups(@PathVariable Long taskId) {
        Optional<Task> taskOpt = taskService.findById(taskId);
        if (taskOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<TaskSignup> signups = taskSignupService.getTaskSignups(taskId);

        if (signups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(signups, HttpStatus.OK);
    }

    /**
     * Sign up a volunteer for a task.
     *
     * @param request TaskSignupDto containing volunteer and task IDs.
     * @return Created TaskSignup or HTTP 404 if task or volunteer not found.
     */
    @PostMapping
    public ResponseEntity<TaskSignup> signUpForTask(@RequestBody @Valid TaskSignupDto request) {
        try {
            Optional<Task> taskOptional = taskService.findById(request.getTaskId());
            Optional<Volunteer> userOptional = userService.findVolunteerById(request.getVolunteerId());

            if (taskOptional.isEmpty() || userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Task task = taskOptional.get();
            Volunteer volunteer = userOptional.get();

            if (task.getStatus() == TaskStatus.CANCELLED || task.getStatus() == TaskStatus.APPLICATION_ENDED) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Optional<TaskSignup> existingSignup = taskSignupService.findByTaskIdAndVolunteerId(task.getId(), volunteer.getId());
            if (existingSignup.isPresent()) {
                return new ResponseEntity<>(existingSignup.get(), HttpStatus.FOUND); // Return existing if signup already exists
            }

            TaskSignup taskSignup = TaskSignup.builder()
                    .task(task)
                    .volunteer(volunteer)
                    .build();

            taskSignupService.save(taskSignup);
            return new ResponseEntity<>(taskSignup, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Cancel a volunteer's signup for a task by volunteer ID and task ID.
     *
     * @param volunteerId Volunteer ID.
     * @param taskId Task ID.
     * @return Success message or HTTP 404 if signup not found.
     */
    @DeleteMapping("/volunteer/{volunteerId}/task/{taskId}")
    public ResponseEntity<Void> cancelSignup(@PathVariable Long volunteerId, @PathVariable Long taskId) {
        Optional<TaskSignup> existingSignup = taskSignupService.findByTaskIdAndVolunteerId(taskId, volunteerId);
        if (existingSignup.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TaskSignup signup = existingSignup.get();
        Task task = signup.getTask();

        if (LocalDate.now().isAfter(task.getCancellationDeadline())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        taskSignupService.deleteById(signup.getSignupId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Cancel a task signup by signup ID.
     *
     * @param signupId Signup ID.
     * @return Success message or HTTP 404 if signup not found.
     */
    @DeleteMapping("/{signupId}")
    public ResponseEntity<Void> cancelSignupById(@PathVariable Long signupId) {
        Optional<TaskSignup> existingSignup = taskSignupService.findById(signupId);
        if (existingSignup.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TaskSignup signup = existingSignup.get();
        Task task = signup.getTask();

        if (LocalDate.now().isAfter(task.getCancellationDeadline())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        taskSignupService.deleteById(signupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}