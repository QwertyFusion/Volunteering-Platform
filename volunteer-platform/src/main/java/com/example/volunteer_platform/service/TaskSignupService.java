package com.example.volunteer_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.model.TaskSignup.SignupStatus;
import com.example.volunteer_platform.model.User;
import com.example.volunteer_platform.repository.TaskRepository;
import com.example.volunteer_platform.repository.TaskSignupRepository;
import com.example.volunteer_platform.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskSignupService {

    @Autowired
    private TaskSignupRepository taskSignupRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Sign up for a task
    public Optional<TaskSignup> signUpForTask(Long taskId, Long userId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (taskOptional.isEmpty() || userOptional.isEmpty()) {
            return Optional.empty(); // Return empty if task or user not found
        }

        Task task = taskOptional.get();
        User user = userOptional.get();

        TaskSignup taskSignup = new TaskSignup();
        taskSignup.setTask(task);
        taskSignup.setUser(user);
        taskSignup.setSignupDate(LocalDateTime.now());
        taskSignup.setStatus(SignupStatus.UPCOMING);

        taskSignupRepository.save(taskSignup);
        return Optional.of(taskSignup);
    }

    // Get all signups for a user
    public List<TaskSignup> getUserSignups(Long userId) {
        return taskSignupRepository.findByUserId(userId);
    }

    // Cancel a task signup
    public Optional<TaskSignup> cancelSignup(Long signupId) {
        Optional<TaskSignup> taskSignupOptional = taskSignupRepository.findById(signupId);

        if (taskSignupOptional.isEmpty()) {
            return Optional.empty(); // Return empty if signup not found
        }

        TaskSignup taskSignup = taskSignupOptional.get();
        taskSignup.setStatus(SignupStatus.CANCELLED);
        taskSignupRepository.save(taskSignup);

        return Optional.of(taskSignup);
    }
    
    public TaskSignup save(TaskSignup signup) {
        return taskSignupRepository.save(signup);
    }

}
