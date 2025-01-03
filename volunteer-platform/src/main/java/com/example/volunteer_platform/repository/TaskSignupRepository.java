package com.example.volunteer_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.volunteer_platform.model.TaskSignup;

import java.util.List;

public interface TaskSignupRepository extends JpaRepository<TaskSignup, Long> {
    List<TaskSignup> findByUserId(Long userId);
    List<TaskSignup> findByTaskId(Long taskId);
}
