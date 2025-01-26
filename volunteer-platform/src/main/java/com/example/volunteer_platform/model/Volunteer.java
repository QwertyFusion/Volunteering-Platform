package com.example.volunteer_platform.model;

import java.util.Set;

import com.example.volunteer_platform.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Volunteer class represents a volunteer user in the system, extending the User class.
 */
@Entity
@DiscriminatorValue("VOLUNTEER")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Volunteer extends User {

    @NotNull
    @Enumerated(EnumType.STRING) // Persist the enum as a string in the database
    @Column(nullable = false)
    private Gender gender; // Gender of the volunteer

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "volunteer_skills", // Join table name
            joinColumns = @JoinColumn(name = "volunteer_id"), // Column in the join table referencing Volunteer
            inverseJoinColumns = @JoinColumn(name = "skill_id") // Column in the join table referencing Skill
    )
    private Set<Skill> skills; // Set of skills for the volunteer
}