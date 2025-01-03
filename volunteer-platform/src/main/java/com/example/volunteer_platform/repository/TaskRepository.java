package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.Task.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find tasks by organization ID
    List<Task> findByOrganizationId(Long organizationId);

    // Find tasks by title containing (case-insensitive)
    List<Task> findByTitleContainingIgnoreCase(String title);

    // Find tasks by location containing (case-insensitive)
    List<Task> findByLocationContainingIgnoreCase(String location);

    // Find tasks by description containing (case-insensitive)
    List<Task> findByDescriptionContainingIgnoreCase(String description);

    // Find tasks by status
    List<Task> findByStatus(TaskStatus status);

    // Find tasks with deadlines after a certain date, ordered by deadline ascending
    List<Task> findByEndDateTimeAfterOrderByEndDateTimeAsc(LocalDateTime dateTime);
}
