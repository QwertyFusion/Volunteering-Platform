package com.example.volunteer_platform.scheduler;

import com.example.volunteer_platform.service.EmailService;
import com.example.volunteer_platform.repository.TaskSignupRepository;
import com.example.volunteer_platform.model.TaskSignup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ReminderScheduler.class);

    private final TaskSignupRepository taskSignupRepository;
    private final EmailService emailService;

    @Transactional
    // @Scheduled(cron = "0 0 8 * * *") // Uncomment for scheduling
    public void sendTaskReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1); // Get tomorrow's date

        // Find all task signups where reminders haven't been sent and task is upcoming
        List<TaskSignup> upcomingTasks = taskSignupRepository.findUpcomingTaskSignups(
            tomorrow, false // Only tasks with reminderSent = false
        );

        logger.info("Found {} upcoming task signups", upcomingTasks.size()); // Log the number of signups

        for (TaskSignup signup : upcomingTasks) {
            logger.info("Processing TaskSignup ID: {}", signup.getSignupId()); // Log the current signup ID

            String emailBody = String.format(
            	    "Dear %s,\n\n" +
            	    "Thank you for signing up as a volunteer with us! This is a friendly reminder about your upcoming task:\n\n" +
            	    "Task Title: %s\n" +
            	    "Date: %s\n" +
            	    "Location: %s\n" +
            	    "Description: %s\n\n" +
            	    "We appreciate your dedication and commitment to making a difference.\n\n" +
            	    "If you have any questions or need further assistance, feel free to contact us.\n\n" +
            	    "Thank you once again for your time and efforts.\n\n" +
            	    "Warm regards,\n" +
            	    "The Volunteer Platform Team",
            	    signup.getVolunteer().getName(),  // Volunteer Name
            	    signup.getTask().getTitle(),      // Task Title
            	    signup.getTask().getEventDate(),  // Event Date
            	    signup.getTask().getLocation(),   // Task Location
            	    signup.getTask().getDescription() // Task Description
            	);


            try {
                // Send email
                emailService.sendReminderEmail(
                    signup.getVolunteer().getEmail(),
                    "Reminder: Upcoming Volunteer Task - " + signup.getTask().getTitle(),
                    emailBody
                );

                // Mark reminder as sent and save to the database
                signup.setReminderSent(true);
                taskSignupRepository.save(signup); // Save the updated signup

                // Log successful email
                logger.info("Reminder sent for TaskSignup ID: {}", signup.getSignupId());

                // Ensure immediate database synchronization
                taskSignupRepository.flush();
            } catch (Exception e) {
                logger.error("Failed to send email to: {}", signup.getVolunteer().getEmail(), e);
            }
        }
    }
}
