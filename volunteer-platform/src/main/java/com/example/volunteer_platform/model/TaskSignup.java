package com.example.volunteer_platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_signup")
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
@AllArgsConstructor
@Builder
public class TaskSignup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signupId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Volunteer volunteer;

    @Column(name = "signup_date", nullable = false, updatable = false)
    private LocalDateTime signupDate;

    @Column(nullable = false, updatable = false)
    private LocalDate cancellationDeadline; // Input by task creator (org) in the format "yyyy-MM-dd", later parsed into correct format.

    @Column(nullable = false)
    private boolean reminderSent; // Default value is false

    // TASK SIGNUP WILL NOT EXIST IF VOLUNTEER DID NOT SIGN UP --> Thus no need for signup status. If cancels, then this entity will get deleted.

    // PrePersist hook to automatically set the signupDate when a new signup is created
    @PrePersist
    public void prePersist() {
        this.signupDate = LocalDateTime.now();
        this.reminderSent = false;
    }
}