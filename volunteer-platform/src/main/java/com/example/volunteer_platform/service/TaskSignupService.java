package com.example.volunteer_platform.service;

import com.example.volunteer_platform.dto.ReminderStatusDTO;
import com.example.volunteer_platform.model.TaskSignup;

import java.util.List;
import java.util.Optional;

/**
 * TaskSignupService provides methods to manage task signups in the system.
 */
public interface TaskSignupService {

    /**
     * Get all available task signups.
     *
     * @return List of task signups.
     */
    List<TaskSignup> getAllSignups();

    /**
     * Get all signups for a specific volunteer.
     *
     * @param volunteerId Volunteer ID.
     * @return List of task signups for the volunteer.
     */
    List<TaskSignup> getUserSignups(Long volunteerId);

    /**
     * Get all signups for a specific task.
     *
     * @param taskId Task ID.
     * @return List of task signups for the task.
     */
    List<TaskSignup> getTaskSignups(Long taskId);

    /**
     * Find a task signup by its ID.
     *
     * @param signupId Task signup ID.
     * @return Optional containing the task signup if found.
     */
    Optional<TaskSignup> findById(Long signupId);

    /**
     * Get the signup by a volunteer for a specific task.
     *
     * @param taskId Task ID.
     * @param id Volunteer ID.
     * @return Optional containing the task signup if found.
     */
    Optional<TaskSignup> findByTaskIdAndVolunteerId(Long taskId, Long id);
    
    
    
    
    public boolean isVolunteerSignedUpForTask(Long volunteerId, Long taskId);
    
    
    /**
     * Save a task signup to the database.
     *
     * @param taskSignup Task signup to be saved.
     */
    void save(TaskSignup taskSignup);

    /**
     * Delete a task signup by its ID.
     *
     * @param signupId Task signup ID.
     */
    void deleteById(Long signupId);

    /**
     * Get the reminder status for all task signups.
     *
     * @return List of reminder status DTOs.
     */
    List<ReminderStatusDTO> getReminderStatus();

    /**
     * Get the reminder status by signup ID.
     *
     * @param signupId Task signup ID.
     * @return Reminder status DTO.
     */
    ReminderStatusDTO getReminderStatusById(Long signupId);
}