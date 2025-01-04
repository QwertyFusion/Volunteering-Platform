package com.example.volunteer_platform.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.volunteer_platform.model.Organization;
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

    // GET all task signups of a task
    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskSignups(@PathVariable Long taskId) {
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

    // POST task sign up if volunteer signs up for task
    // Request includes:
    //    {
    //        "volunteerId": <volunteer_id_value>,
    //            "taskId": <task_id_value>
    //    }
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

            // Check if the current date is before the application deadline
            if (LocalDate.now().isAfter(task.getApplicationDeadline())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Check if the user has already signed up for the task
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

    // PUT -> Not needed since it is a simple relation based table.
    // EMPTY

    // DELETE task signup if volunteer cancels participation for task
    // Delete by user id and task id
    @DeleteMapping("/volunteer/{volunteerId}/task/{taskId}")
    public ResponseEntity<String> cancelSignup(@PathVariable Long volunteerId, @PathVariable Long taskId) {
        Optional<TaskSignup> existingSignup = taskSignupService.findByTaskIdAndVolunteerId(taskId, volunteerId);
        if (existingSignup.isEmpty()) {
            return new ResponseEntity<>("Volunteer did not sign up for the task!", HttpStatus.NOT_FOUND);
        }
        TaskSignup signup = existingSignup.get();
        Task task = signup.getTask();

        // Check if the current date is before the cancellation deadline
        if (LocalDate.now().isAfter(task.getCancellationDeadline())) {
            return new ResponseEntity<>("Cannot cancel signup after the cancellation deadline.", HttpStatus.BAD_REQUEST);
        }

        taskSignupService.deleteById(signup.getSignupId());
        return new ResponseEntity<>("Cancellation Success: Volunteer no longer signed up for the task.", HttpStatus.NO_CONTENT);
    }

    // Delete by task sign up id
    @DeleteMapping("/{signupId}")
    public ResponseEntity<String> cancelSignupById(@PathVariable Long signupId) {
        Optional<TaskSignup> existingSignup = taskSignupService.findById(signupId);
        if (existingSignup.isEmpty()) {
            return new ResponseEntity<>("No reference found", HttpStatus.NOT_FOUND);
        }
        TaskSignup signup = existingSignup.get();
        Task task = signup.getTask();

        // Check if the current date is before the cancellation deadline
        if (LocalDate.now().isAfter(task.getCancellationDeadline())) {
            return new ResponseEntity<>("Cannot cancel signup after the cancellation deadline.", HttpStatus.BAD_REQUEST);
        }

        taskSignupService.deleteById(signupId);
        return new ResponseEntity<>("Cancellation Success: Volunteer no longer signed up for the task.", HttpStatus.NO_CONTENT);
    }
}