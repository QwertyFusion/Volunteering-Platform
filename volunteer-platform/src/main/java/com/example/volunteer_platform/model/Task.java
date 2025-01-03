package com.example.volunteer_platform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private String skills;

    @NotNull(message = "Start date and time is required")
    @FutureOrPresent(message = "Start date must be in present or future")
    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time is required")
    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.OPEN;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private User organization;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<TaskSignup> signups = new HashSet<>();

    @Column(name = "max_volunteers")
    private Integer maxVolunteers;

    @Column(name = "current_volunteers")
    private Integer currentVolunteers = 0;

    public enum TaskStatus {
        OPEN,
        CLOSED,
        CANCELLED,
        COMPLETED
    }

    // Constructors
    public Task() {
    }

    public Task(String title, String description, String location, String skills,
                LocalDateTime startDateTime, LocalDateTime endDateTime, User organization) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.skills = skills;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.organization = organization;
    }

    // Helper methods
    public boolean isFullyBooked() {
        return maxVolunteers != null && currentVolunteers >= maxVolunteers;
    }

    public boolean hasStarted() {
        return LocalDateTime.now().isAfter(startDateTime);
    }

    public boolean hasEnded() {
        return LocalDateTime.now().isAfter(endDateTime);
    }

    public long getRemainingSpots() {
        if (maxVolunteers == null) return Long.MAX_VALUE;
        return maxVolunteers - currentVolunteers;
    }

    public void incrementVolunteers() {
        if (currentVolunteers == null) currentVolunteers = 0;
        currentVolunteers++;
    }

    public void decrementVolunteers() {
        if (currentVolunteers > 0) {
            currentVolunteers--;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getOrganization() {
        return organization;
    }

    public void setOrganization(User organization) {
        this.organization = organization;
    }

    public Set<TaskSignup> getSignups() {
        return signups;
    }

    public void setSignups(Set<TaskSignup> signups) {
        this.signups = signups;
    }

    public Integer getMaxVolunteers() {
        return maxVolunteers;
    }

    public void setMaxVolunteers(Integer maxVolunteers) {
        this.maxVolunteers = maxVolunteers;
    }

    public Integer getCurrentVolunteers() {
        return currentVolunteers;
    }

    public void setCurrentVolunteers(Integer currentVolunteers) {
        this.currentVolunteers = currentVolunteers;
    }

    // PreUpdate hook to check and update status
    @PreUpdate
    public void preUpdate() {
        if (hasEnded() && status != TaskStatus.CANCELLED) {
            status = TaskStatus.COMPLETED;
        }
    }
}