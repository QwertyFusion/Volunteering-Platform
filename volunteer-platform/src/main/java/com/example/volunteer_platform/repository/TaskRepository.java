package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOrganizationId(Long organizationId);

    List<Task> findByNameContaining(String title);

    List<Task> findByLocationContaining(String location);

    List<Task> findByDescriptionContaining(String description);

//    List<Task> findByNameContainingAndLocationContainingAndDescriptionContaining(String name, String location, String description);
//
//    List<Task> findByNameContainingAndLocationContaining(String name, String location);
//
//    List<Task> findByNameContainingAndDescriptionContaining(String name, String description);
//
//    List<Task> findByLocationContainingAndDescriptionContaining(String location, String description);
}
