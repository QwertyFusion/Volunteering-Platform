package com.example.volunteer_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.repository.TaskSignupRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskSignupService {

    @Autowired
    private TaskSignupRepository taskSignupRepository;

    // Get all available task signups
    public List<TaskSignup> getAllSignups() {
        return taskSignupRepository.findAll();
    }

    // Get all signups for a volunteer
    public List<TaskSignup> getUserSignups(Long volunteerId) {
        return taskSignupRepository.findByVolunteerId(volunteerId);
    }

    // Get all signups for a task
    public List<TaskSignup> getTaskSignups(Long taskId) {
        return taskSignupRepository.findByTaskId(taskId);
    }

    // Get task signup by task signup id
    public Optional<TaskSignup> findById(Long signupId) {
        return taskSignupRepository.findById(signupId);
    }

    // Get the signup by a volunteer for a specific task
    public Optional<TaskSignup> findByTaskIdAndVolunteerId(Long taskId, Long id) {
        return taskSignupRepository.findByTaskIdAndVolunteerId(taskId, id);
    }

    // Save task signup
    public void save(TaskSignup taskSignup) {
        taskSignupRepository.save(taskSignup);
    }

    // Delete task signup by task signup id
    public void deleteById(Long signupId) {
        taskSignupRepository.deleteById(signupId);
    }
}