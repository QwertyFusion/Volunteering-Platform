package com.example.volunteer_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "skills") // This side of the relationship is mapped by the 'skills' field in Task
    private Set<Volunteer> volunteers; // Set of volunteers who have this skill

    @ManyToMany(mappedBy = "skills")
    private Set<Task> tasks; // Set of tasks that require this skill
}