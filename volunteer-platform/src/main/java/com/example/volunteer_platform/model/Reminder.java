//package com.example.volunteer_platform.model;
//
//
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "reminders")
//public class Reminder {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long reminderId;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "signup_id", nullable = false)
//    private TaskSignup taskSignup;
//
//    @Column(nullable = false)
//    private LocalDateTime reminderDate;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private ReminderStatus status;
//
//    // Enum for possible reminder statuses
//    public enum ReminderStatus {
//        SENT,
//        CANCELLED
//    }
//
//    // Getters and Setters
//    public Long getReminderId() {
//        return reminderId;
//    }
//
//    public void setReminderId(Long reminderId) {
//        this.reminderId = reminderId;
//    }
//
//    public TaskSignup getTaskSignup() {
//        return taskSignup;
//    }
//
//    public void setTaskSignup(TaskSignup taskSignup) {
//        this.taskSignup = taskSignup;
//    }
//
//    public LocalDateTime getReminderDate() {
//        return reminderDate;
//    }
//
//    public void setReminderDate(LocalDateTime reminderDate) {
//        this.reminderDate = reminderDate;
//    }
//
//    public ReminderStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(ReminderStatus status) {
//        this.status = status;
//    }
//
//    // PrePersist hook to set reminderDate
//    @PrePersist
//    public void prePersist() {
//        if (this.reminderDate == null) {
//            this.reminderDate = LocalDateTime.now(); // Adjust this to match the reminder time logic
//        }
//    }
//}
