package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.TaskSignup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing TaskSignup entities.
 */
public interface TaskSignupRepository extends JpaRepository<TaskSignup, Long> {

    /**
     * Find signups by volunteer ID.
     *
     * @param volunteerId Volunteer ID.
     * @return List of task signups for the specified volunteer.
     */
    List<TaskSignup> findByVolunteerId(Long volunteerId);

    /**
     * Find signups by task ID.
     *
     * @param taskId Task ID.
     * @return List of task signups for the specified task.
     */
    List<TaskSignup> findByTaskId(Long taskId);

    /**
     * Find signups for tasks within a specific time range.
     *
     * @param startDate Start date for the range.
     * @param endDate End date for the range.
     * @return List of task signups within the specified date range.
     */
    List<TaskSignup> findBySignupDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find a task signup by task ID and volunteer ID.
     *
     * @param taskId Task ID.
     * @param volunteerId Volunteer ID.
     * @return Optional containing the task signup if found.
     */
    Optional<TaskSignup> findByTaskIdAndVolunteerId(Long taskId, Long volunteerId);
}