package com.example.volunteer_platform.service;

import com.example.volunteer_platform.model.Skill;

import java.util.List;
import java.util.Optional;

/**
 * SkillService provides methods to manage skills in the system.
 */
public interface SkillService {

    /**
     * Get all skills in the system.
     *
     * @return List of skills.
     */
    List<Skill> getAllSkills();

    /**
     * Find a skill by its name.
     *
     * @param name Name of the skill.
     * @return Optional containing the skill if found.
     */
    Optional<Skill> findByName(String name);

    /**
     * Find a skill by its ID.
     *
     * @param skillId Skill ID.
     * @return Optional containing the skill if found.
     */
    Optional<Skill> findById(Long skillId);

    /**
     * Save a skill to the database.
     *
     * @param skill Skill to be saved.
     */
    void saveSkill(Skill skill);

    /**
     * Delete a skill by its ID (admin only).
     *
     * @param skillId Skill ID.
     */
    void deleteSkillById(Long skillId);
}