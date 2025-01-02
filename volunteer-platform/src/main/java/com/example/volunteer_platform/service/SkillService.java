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

    // 1. Get all skills
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    // 2. Get skill by ID
    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    // 3. Add new skill (or update existing skill)
    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    // 4. Delete skill by ID
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    // Additional Business Logic (if needed)
    // You can add any other necessary methods here, such as matching skills to tasks or volunteers.
}
