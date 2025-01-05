package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Skill entities.
 */
public interface SkillRepository extends JpaRepository<Skill, Long> {

    /**
     * Find a skill by its name.
     *
     * @param name Name of the skill.
     * @return Optional containing the skill if found.
     */
    Optional<Skill> findByName(String name);
}