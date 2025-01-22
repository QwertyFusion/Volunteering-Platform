package com.example.volunteer_platform.service.implementation;

import com.example.volunteer_platform.model.Skill;
import com.example.volunteer_platform.repository.SkillRepository;
import com.example.volunteer_platform.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * SkillServiceImplementation provides methods to manage skills in the system.
 * This is an implementation of the SkillService interface.
 */
@Service
public class SkillServiceImplementation implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }

    @Override
    public Optional<Skill> findById(Long skillId) {
        return skillRepository.findById(skillId);
    }

    @Override
    public void saveSkill(Skill skill) {
        skillRepository.save(skill);
    }

    @Override
    public void deleteSkillById(Long skillId) {
        skillRepository.deleteById(skillId);
    }
}