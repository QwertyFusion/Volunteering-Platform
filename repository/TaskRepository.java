
package com.example.volunteer_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.volunteer_platform.model.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

	List<Task> findByOrganizationId(Long organizationId);

	List<Task> findByTitleContaining(String title);

	List<Task> findByDescriptionContaining(String description);

	List<Task> findByLocationContaining(String location);

}