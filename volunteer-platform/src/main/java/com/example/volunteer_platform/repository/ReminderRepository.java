package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.Reminder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    // Add any custom queries if needed
	
	List<Reminder> findByStatus(Reminder.ReminderStatus status);
}
