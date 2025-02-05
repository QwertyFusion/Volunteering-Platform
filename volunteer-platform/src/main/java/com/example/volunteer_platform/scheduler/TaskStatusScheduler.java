package com.example.volunteer_platform.scheduler;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.repository.TaskRepository;
import com.example.volunteer_platform.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskStatusScheduler {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Scheduled method to update task statuses to APPLICATION_ENDED if the application deadline has passed.
     */
    @Scheduled(cron = "0 0 0 * * *") // This will run every day at midnight
    public void updateTaskStatuses() {
        List<Task> tasks = taskRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            // Check if the application deadline has passed and the task is available
            if (task.getApplicationDeadline().isBefore(today)
                    && task.getStatus() == TaskStatus.AVAILABLE) {
                task.setStatus(TaskStatus.APPLICATION_ENDED);
                taskRepository.save(task);
            }

            if (task.getEventDate().isBefore(today)
                    && task.getStatus() != TaskStatus.ENDED) {
                task.setStatus(TaskStatus.ENDED);
                taskRepository.save(task);
            }
        }
    }
}