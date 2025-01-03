package com.example.volunteer_platform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@DiscriminatorValue("VOLUNTEER")
@Data
@NoArgsConstructor
public class Volunteer extends User {

    @ManyToMany
    @JoinTable(
            name = "volunteer_skills", // Join table name
            joinColumns = @JoinColumn(name = "volunteer_id"), // Column in the join table referencing Volunteer
            inverseJoinColumns = @JoinColumn(name = "skill_id") // Column in the join table referencing Skill
    )
    private Set<Skill> skills; // Set of skills for the volunteer

    // Additional volunteer-specific fields can be added here
}