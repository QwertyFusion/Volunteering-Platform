package com.example.volunteer_platform.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.volunteer_platform.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {

	
}
