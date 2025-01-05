package com.example.volunteer_platform.service;

import com.example.volunteer_platform.model.Skill;
import com.example.volunteer_platform.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * SkillService provides methods to manage skills in the system.
 */
@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Get all skills in the system.
     *
     * @return List of skills.
     */
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    /**
     * Find a skill by its name.
     *
     * @param name Name of the skill.
     * @return Optional containing the skill if found.
     */
    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }

    /**
     * Find a skill by its ID.
     *
     * @param skillId Skill ID.
     * @return Optional containing the skill if found.
     */
    public Optional<Skill> findById(Long skillId) {
        return skillRepository.findById(skillId);
    }

    /**
     * Save a skill to the database.
     *
     * @param skill Skill to be saved.
     */
    public void saveSkill(Skill skill) {
        skillRepository.save(skill);
    }

    /**
     * Delete a skill by its ID (admin only).
     *
     * @param skillId Skill ID.
     */
    public void deleteSkillById(Long skillId) {
        skillRepository.deleteById(skillId);
    }
}
