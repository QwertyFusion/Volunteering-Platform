package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.dto.TaskSignupDto;
import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.service.TaskService;
import com.example.volunteer_platform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.service.TaskSignupService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task-signups")
public class TaskSignupController {

    @Autowired
    private TaskSignupService taskSignupService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    // GET all volunteers signed up for a task
    @GetMapping
    public ResponseEntity<?> getAllSignups() {
        List<TaskSignup> signups = taskSignupService.getAllSignups();

        if (signups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(signups, HttpStatus.OK);
    }

    // GET all task signups by a volunteer
    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<?> getUserSignups(@PathVariable Long volunteerId) {
        List<TaskSignup> signups = taskSignupService.getUserSignups(volunteerId);

        if (signups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(signups, HttpStatus.OK);
    }

    // GET all task signups of a task
    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskSignups(@PathVariable Long taskId) {
        List<TaskSignup> signups = taskSignupService.getTaskSignups(taskId);

        if (signups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(signups, HttpStatus.OK);
    }

    // POST task sign up if volunteer signs up for task
    // Request includes:
    //    {
    //        "volunteerId": <volunteer_id_value>,
    //            "taskId": <task_id_value>,
    //            "cancellationDeadline": "<Date in the format: yyyy-MM-dd>"
    //    }
    @PostMapping
    public ResponseEntity<?> signUpForTask(@RequestBody @Valid TaskSignupDto request, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<Task> taskOptional = taskService.findById(request.getTaskId());
            Optional<Volunteer> userOptional = userService.findVolunteerById(request.getVolunteerId());

            if (taskOptional.isEmpty() || userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Task task = taskOptional.get();
            Volunteer volunteer = userOptional.get();

            // Check if the user has already signed up for the task
            Optional<TaskSignup> existingSignup = taskSignupService.findByTaskIdAndVolunteerId(task.getId(), volunteer.getId());
            if (existingSignup.isPresent()) {
                return new ResponseEntity<>(existingSignup.get(), HttpStatus.FOUND); // Return existing if signup already exists
            }

            TaskSignup taskSignup = TaskSignup.builder()
                    .task(task)
                    .volunteer(volunteer)
                    .cancellationDeadline(request.getCancellationDeadline()) // No parsing needed now
                    .build();

            taskSignupService.save(taskSignup);
            return new ResponseEntity<>(taskSignup, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT -> Not needed since it is a simple relation based table.
    // EMPTY

    // DELETE task signup if volunteer cancels participation for task
    // Delete by user id and task id
    @DeleteMapping("/volunteer/{volunteerId}/task/{taskId}")
    public ResponseEntity<?> cancelSignup(@PathVariable Long volunteerId, @PathVariable Long taskId) {
        Optional<TaskSignup> existingSignup = taskSignupService.findByTaskIdAndVolunteerId(taskId, volunteerId);
        if (existingSignup.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return existing if signup already exists
        }
        taskSignupService.deleteById(existingSignup.get().getSignupId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete by task sign up id
    @DeleteMapping("/{signupId}")
    public ResponseEntity<?> cancelSignupById(@PathVariable Long signupId) {
        Optional<TaskSignup> existingSignup = taskSignupService.findById(signupId);
        if (existingSignup.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        taskSignupService.deleteById(signupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}