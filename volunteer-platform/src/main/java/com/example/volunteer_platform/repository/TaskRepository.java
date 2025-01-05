package com.example.volunteer_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.volunteer_platform.model.Task;

/**
 * Repository interface for managing Task entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find tasks by title containing a specific string.
     *
     * @param title Title substring to search for.
     * @return List of tasks matching the title criteria.
     */
    List<Task> findByTitleContaining(String title);

    /**
     * Find tasks by description containing a specific string.
     *
     * @param description Description substring to search for.
     * @return List of tasks matching the description criteria.
     */
    List<Task> findByDescriptionContaining(String description);

    /**
     * Find tasks by location containing a specific string.
     *
     * @param location Location substring to search for.
     * @return List of tasks matching the location criteria.
     */
    List<Task> findByLocationContaining(String location);
}