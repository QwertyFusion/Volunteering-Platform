package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.TaskSignup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskSignupRepository extends JpaRepository<TaskSignup, Long> {
    
    // Find signups by user ID
    List<TaskSignup> findByUserId(Long userId);
    
    // Find signups by task ID
    List<TaskSignup> findByTaskId(Long taskId);
    
    // Custom query to find signups for tasks within a specific time range
    List<TaskSignup> findBySignupDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
