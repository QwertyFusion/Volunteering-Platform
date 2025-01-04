package com.example.volunteer_platform.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.example.volunteer_platform.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name="tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false)
	private String title;

	@NotBlank
	@Column(nullable = false)
	private String description;

	@NotBlank
	@Size(max = 100)
	private String location;

	@NotBlank
	@Future
	private LocalDate eventDate; // When the event will be hosted. We need this to figure out the time when we send notification. Format: 2nd October 2007

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Enumerated(EnumType.STRING) // Store the enum as a string in the database
	@Column(nullable = false)
	private TaskStatus status; // Use the TaskStatus enum

	@ManyToOne
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;

	@ManyToMany
	@JoinTable(
			name = "task_skills", // Join table name
			joinColumns = @JoinColumn(name = "task_id"), // Foreign key for Task
			inverseJoinColumns = @JoinColumn(name = "skill_id") // Foreign key for Skill
	)
	private Set<Skill> skills; // Set of skills required for the task

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TaskSignup> signups;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
		this.status = TaskStatus.AVAILABLE;
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}