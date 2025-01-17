package com.example.volunteer_platform.controller;


import com.example.volunteer_platform.dto.ReminderStatusDTO;
import com.example.volunteer_platform.scheduler.ReminderScheduler;
import com.example.volunteer_platform.service.TaskSignupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test/reminders")
@RequiredArgsConstructor
public class ReminderTestController {
    private final ReminderScheduler reminderScheduler;
    private final TaskSignupService taskSignupService;

    // Trigger reminders manually
    @PostMapping("/send")
    @Transactional
    public ResponseEntity<String> triggerReminders() {
        try {
            reminderScheduler.sendTaskReminders();
            return ResponseEntity.ok("Reminder check triggered successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error sending reminders: " + e.getMessage());
        }
    }

    // Check reminder status for all signups
    @GetMapping("/status")
    public ResponseEntity<List<ReminderStatusDTO>> checkReminderStatus() {
        List<ReminderStatusDTO> status = taskSignupService.getReminderStatus();
        return ResponseEntity.ok(status);
    }

    // Check reminder status for specific signup
    @GetMapping("/status/{signupId}")
    public ResponseEntity<ReminderStatusDTO> checkSignupReminderStatus(@PathVariable Long signupId) {
        ReminderStatusDTO status = taskSignupService.getReminderStatusById(signupId);
        return ResponseEntity.ok(status);
    }
}