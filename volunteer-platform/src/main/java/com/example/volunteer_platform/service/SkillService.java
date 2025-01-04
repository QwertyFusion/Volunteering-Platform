package com.example.volunteer_platform.service;

import com.example.volunteer_platform.model.Skill;
import com.example.volunteer_platform.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    // Get all skills
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    // Get skill by name
    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }

    // Get skill by id
    public Optional<Skill> findById(Long skillId) {
        return skillRepository.findById(skillId);
    }

    // Save skill
    public void saveSkill(Skill skill) {
        skillRepository.save(skill);
    }
}
