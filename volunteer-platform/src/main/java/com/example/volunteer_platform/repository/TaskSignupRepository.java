package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.TaskSignup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
    
   // @Query("SELECT t.task FROM TaskSignup t WHERE t.volunteer.id = :volunteerId AND t.task.completed = true")
    //List<Task> findCompletedTasksByVolunteerId(@Param("volunteerId") Long volunteerId);

    
    boolean existsByVolunteerIdAndTaskId(Long volunteerId, Long taskId);

    /**
     * Find upcoming task signups for tasks happening on a specific event date
     * where reminders haven't been sent yet.
     *
     * @param eventDate The date of the event.
     * @return List of task signups matching the criteria.
     */
     @Query("SELECT ts FROM TaskSignup ts JOIN ts.task t WHERE t.eventDate = :eventDate AND ts.reminderSent = false")
    List<TaskSignup> findUpcomingTaskSignups(@Param("eventDate") LocalDate eventDate, @Param("reminderSent") boolean reminderSent);
}
