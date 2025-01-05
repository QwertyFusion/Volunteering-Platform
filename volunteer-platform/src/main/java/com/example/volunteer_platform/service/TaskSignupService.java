package com.example.volunteer_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.repository.TaskSignupRepository;

import java.util.List;
import java.util.Optional;

/**
 * TaskSignupService provides methods to manage task signups in the system.
 */
@Service
public class TaskSignupService {

    @Autowired
    private TaskSignupRepository taskSignupRepository;

    /**
     * Get all available task signups.
     *
     * @return List of task signups.
     */
    public List<TaskSignup> getAllSignups() {
        return taskSignupRepository.findAll();
    }

    /**
     * Get all signups for a specific volunteer.
     *
     * @param volunteerId Volunteer ID.
     * @return List of task signups for the volunteer.
     */
    public List<TaskSignup> getUserSignups(Long volunteerId) {
        return taskSignupRepository.findByVolunteerId(volunteerId);
    }

    /**
     * Get all signups for a specific task.
     *
     * @param taskId Task ID.
     * @return List of task signups for the task.
     */
    public List<TaskSignup> getTaskSignups(Long taskId) {
        return taskSignupRepository.findByTaskId(taskId);
    }

    /**
     * Find a task signup by its ID.
     *
     * @param signupId Task signup ID.
     * @return Optional containing the task signup if found.
     */
    public Optional<TaskSignup> findById(Long signupId) {
        return taskSignupRepository.findById(signupId);
    }

    /**
     * Get the signup by a volunteer for a specific task.
     *
     * @param taskId Task ID.
     * @param id Volunteer ID.
     * @return Optional containing the task signup if found.
     */
    public Optional<TaskSignup> findByTaskIdAndVolunteerId(Long taskId, Long id) {
        return taskSignupRepository.findByTaskIdAndVolunteerId(taskId, id);
    }

    /**
     * Save a task signup to the database.
     *
     * @param taskSignup Task signup to be saved.
     */
    public void save(TaskSignup taskSignup) {
        taskSignupRepository.save(taskSignup);
    }

    /**
     * Delete a task signup by its ID.
     *
     * @param signupId Task signup ID.
     */
    public void deleteById(Long signupId) {
        taskSignupRepository.deleteById(signupId);
    }
}