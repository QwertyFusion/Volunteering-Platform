package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.model.Skill;
import com.example.volunteer_platform.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    // 1. Get all skills
    @GetMapping
    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    // 2. Get skill by ID
    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") Long id) {
        Optional<Skill> skill = skillService.getSkillById(id);
        if (skill.isPresent()) {
            return ResponseEntity.ok(skill.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Create new skill
    @PostMapping
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill) {
        Skill createdSkill = skillService.addSkill(skill);
        return ResponseEntity.status(201).body(createdSkill);
    }

    // 4. Update existing skill
    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable("id") Long id, @RequestBody Skill skillDetails) {
        Optional<Skill> existingSkill = skillService.getSkillById(id);
        if (existingSkill.isPresent()) {
            Skill updatedSkill = existingSkill.get();
            updatedSkill.setName(skillDetails.getName());  // Only update name for now
            return ResponseEntity.ok(skillService.addSkill(updatedSkill));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Delete skill by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id) {
        Optional<Skill> existingSkill = skillService.getSkillById(id);
        if (existingSkill.isPresent()) {
            skillService.deleteSkill(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
