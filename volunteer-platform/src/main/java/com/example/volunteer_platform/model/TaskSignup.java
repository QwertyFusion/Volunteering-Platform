package com.example.volunteer_platform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_signup")
public class TaskSignup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signupId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "signup_date", nullable = false, updatable = false)
    private LocalDateTime signupDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SignupStatus status;

    // Enum for possible signup statuses
    public enum SignupStatus {
        UPCOMING,
        COMPLETED,
        CANCELLED
    }

    // Getters and Setters
    public Long getSignupId() {
        return signupId;
    }

    public void setSignupId(Long signupId) {
        this.signupId = signupId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }

    public SignupStatus getStatus() {
        return status;
    }

    public void setStatus(SignupStatus status) {
        this.status = status;
    }

    // PrePersist hook to automatically set the signupDate when a new signup is created
    @PrePersist
    public void prePersist() {
        if (this.signupDate == null) {
            this.signupDate = LocalDateTime.now();
        }
    }

    
}
