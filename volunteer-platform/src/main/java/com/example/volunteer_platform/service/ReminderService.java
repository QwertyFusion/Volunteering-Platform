//package com.example.volunteer_platform.service;
//
//import com.example.volunteer_platform.model.Reminder;
//import com.example.volunteer_platform.model.Task;
//import com.example.volunteer_platform.model.TaskSignup;
//import com.example.volunteer_platform.model.User;
//import com.example.volunteer_platform.repository.ReminderRepository;
//import com.example.volunteer_platform.repository.TaskSignupRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class ReminderService {
//
//    private static final Logger logger = LoggerFactory.getLogger(ReminderService.class);
//
//    @Autowired
//    private ReminderRepository reminderRepository;
//
//    @Autowired
//    private TaskSignupRepository taskSignupRepository;
//
//    @Autowired
//    private EmailService emailService; // Inject the EmailService to use the sendEmail method
//
//    // This method sends reminders 24 hours before the task date
//    @Scheduled(cron = "0 0 12 * * ?") // Run daily at noon
//    public void sendReminders() {
//        try {
//            // Get signups for tasks that are within the next 24 hours
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime oneDayLater = now.plusDays(1);
//
//            List<TaskSignup> signups = taskSignupRepository.findBySignupDateBetween(now, oneDayLater);
//
//            for (TaskSignup signup : signups) {
//                // Create and save a reminder
//                Reminder reminder = new Reminder();
//                reminder.setTaskSignup(signup);
//                reminder.setReminderDate(oneDayLater.minusHours(24)); // Remind 24 hours before task date
//                reminder.setStatus(Reminder.ReminderStatus.SENT);
//                reminderRepository.save(reminder);
//
//                // Send the email or notification (using sendEmail)
//                sendEmailNotification(signup.getUser(), signup.getTask());
//            }
//        } catch (Exception e) {
//            logger.error("Error occurred while sending reminders: ", e);
//        }
//    }
//
//    // Send Email Notification Logic
//    private void sendEmailNotification(User user, Task task) {
//        try {
//            String subject = "Reminder: Upcoming Volunteer Task - " + task.getTitle();
//            String message = "Hello " + user.getName() + ",\n\n" +
//                             "This is a reminder that you are signed up for the task: " + task.getTitle() + "\n" +
//                             "Date: " + task.getDate() + "\n" +
//                             "Location: " + task.getLocation() + "\n" +
//                             "Please make sure to attend.\n\n" +
//                             "Thank you!";
//                             
//            emailService.sendEmail(user.getEmail(), subject, message); // Calling the sendEmail method
//            logger.info("Reminder sent to user: " + user.getEmail());
//        } catch (Exception e) {
//            logger.error("Error occurred while sending email to user: " + user.getEmail(), e);
//        }
//    }
//}
