package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.dto.SkillDto;
import com.example.volunteer_platform.model.Skill;
import com.example.volunteer_platform.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    // GET all skills
    @GetMapping
    public ResponseEntity<?> getAllSkills() {
        List<Skill> skills = skillService.getAllSkills();

        if (skills.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    // GET Skill by name
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getSkillByName(@PathVariable String name) {
        Skill skill = skillService.findByName(name).orElse(null);
        if (skill != null) {
            return new ResponseEntity<>(skill, HttpStatus.OK);
        } else {
            // Create new skill using SkillDto
            SkillDto newSkillDto = new SkillDto(name);
            return saveSkill(newSkillDto); // Call saveSkill method
        }
    }

    // POST or save a skill
    @PostMapping
    public ResponseEntity<?> saveSkill(@RequestBody SkillDto skillDto) {
        try {
            Skill skill = Skill.builder().name(skillDto.getName()).build();
            skillService.saveSkill(skill);
            return new ResponseEntity<>(skill, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
