package com.example.volunteer_platform.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.volunteer_platform.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Task class represents a task that volunteers can sign up for.
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Size(max = 100)
	private String location;

	@Future
	@Column(nullable = false)
	private LocalDate eventDate; // When the event will be hosted. Format is "yyyy-MM-dd"

	@Column(nullable = false, updatable = false)
	private LocalDate cancellationDeadline; // Deadline for task cancellation. Format is "yyyy-MM-dd"

	@Column(nullable = false, updatable = false)
	private LocalDate applicationDeadline; // Deadline for task applications. Format is "yyyy-MM-dd"

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Enumerated(EnumType.STRING) // Store the enum as a string in the database
	@Column(nullable = false)
	private TaskStatus status; // Use the TaskStatus enum

 	private Long organizationId;

	@ManyToMany
	@JoinTable(
			name = "task_skills", // Join table name
			joinColumns = @JoinColumn(name = "task_id"), // Foreign key for Task
			inverseJoinColumns = @JoinColumn(name = "skill_id") // Foreign key for Skill
	)
	private Set<Skill> skills; // Set of skills required for the task

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now; // Set created date and time to now
		this.updatedAt = now;
		this.status = TaskStatus.AVAILABLE; // Default status when created
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}