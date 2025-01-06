package com.example.volunteer_platform.dto;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ReminderStatusDTO {
    private Long signupId;
    private String taskTitle;
    private String volunteerEmail;
    private LocalDate eventDate;
    private boolean reminderSent;
    private LocalDateTime lastReminderSentAt;
}
