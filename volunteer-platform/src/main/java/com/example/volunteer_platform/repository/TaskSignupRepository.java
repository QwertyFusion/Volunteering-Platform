package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.TaskSignup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskSignupRepository extends JpaRepository<TaskSignup, Long> {

    // Find signups by user ID
    List<TaskSignup> findByVolunteerId(Long volunteerId);

    // Find signups by task ID
    List<TaskSignup> findByTaskId(Long taskId);

    // Custom query to find signups for tasks within a specific time range
    List<TaskSignup> findBySignupDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Optional<TaskSignup> findByTaskIdAndVolunteerId(Long task_id, Long volunteerId);


    @Query("SELECT ts FROM TaskSignup ts JOIN ts.task t WHERE t.eventDate = :eventDate AND ts.reminderSent = false")
    List<TaskSignup> findUpcomingTaskSignups(@Param("eventDate") LocalDate eventDate, @Param("reminderSent") boolean reminderSent);
}