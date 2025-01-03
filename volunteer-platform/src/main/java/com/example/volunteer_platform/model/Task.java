package com.example.volunteer_platform.model;

import java.time.LocalDate;

import jakarta.persistence.*;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String title;
	private String description;
	private String location;
	private String skills;
	private LocalDate date;
	private String time;
	private String status; // Open, Closed, Cancelled

	@ManyToOne
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;
}