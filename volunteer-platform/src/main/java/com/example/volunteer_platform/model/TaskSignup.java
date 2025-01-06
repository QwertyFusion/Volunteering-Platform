package com.example.volunteer_platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TaskSignup class represents the signup of a volunteer for a specific task.
 */
@Entity
@Table(name = "task_signup")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSignup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signupId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task; // The task the volunteer signed up for

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Volunteer volunteer; // The volunteer who signed up

    @Column(name = "signup_date", nullable = false, updatable = false)
    private LocalDateTime signupDate; // Date and time of signup

    @Column(nullable = false)
    private boolean reminderSent; // Indicates if a reminder has been sent

    @PrePersist
    public void prePersist() {
        this.signupDate = LocalDateTime.now(); // Set signup date to now
        this.reminderSent = false; // Default value for reminderSent
    }
}