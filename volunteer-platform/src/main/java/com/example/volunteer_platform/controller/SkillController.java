package com.example.volunteer_platform.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.volunteer_platform.dto.SkillDto;
import com.example.volunteer_platform.model.Skill;
import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.service.SkillService;
import com.example.volunteer_platform.service.TaskService;
import com.example.volunteer_platform.service.UserService;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing skills in the community volunteering platform.
 * Provides APIs for CRUD operations on skills and their associations with tasks and volunteers.
 */
@RestController
@RequestMapping
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    /**
     * Retrieves all skills.
     *
     * @return List of skills or HTTP 204 if no skills exist.
     */
    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> skills = skillService.getAllSkills();
        if (skills.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    /**
     * Retrieves a skill by its name. Creates the skill if it does not exist.
     *
     * @param name Name of the skill.
     * @return Skill object or newly created skill.
     */
    @GetMapping("/skills/name/{name}")
    public ResponseEntity<Skill> getSkillByName(@PathVariable String name) {
        Skill skill = skillService.findByName(name).orElse(null);
        if (skill != null) {
            return new ResponseEntity<>(skill, HttpStatus.OK);
        } else {
            SkillDto newSkillDto = new SkillDto(name);
            return saveSkill(newSkillDto);
        }
    }

    /**
     * Retrieves a skill by its ID.
     *
     * @param id ID of the skill.
     * @return Skill object or HTTP 404 if not found.
     */
    @GetMapping("/skills/id/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) {
        Skill skill = skillService.findById(id).orElse(null);
        if (skill != null) {
            return new ResponseEntity<>(skill, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates or saves a new skill.
     *
     * @param skillDto Data transfer object containing skill details.
     * @return Created skill or HTTP 409 if a conflict occurs.
     */
    @PostMapping("/skills")
    @Transactional
    public ResponseEntity<Skill> saveSkill(@RequestBody @Valid SkillDto skillDto) {
        if (skillService.findByName(skillDto.getName()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        try {
            Skill skill = Skill.builder().name(skillDto.getName().toLowerCase()).build();
            skillService.saveSkill(skill);
            return new ResponseEntity<>(skill, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a skill by its ID.
     *
     * @param id ID of the skill to delete.
     * @return HTTP 204 if deleted or HTTP 404 if not found.
     */
    @DeleteMapping("/skills/id/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSkillById(@PathVariable Long id) {
        Skill skill = skillService.findById(id).orElse(null);
        if (skill != null) {
            skillService.deleteSkillById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Volunteer Skills APIs
    /**
     * Retrieves all skills associated with a volunteer.
     *
     * @param volunteerId ID of the volunteer.
     * @return List of skills or HTTP 404 if volunteer not found.
     */
    @GetMapping("/volunteers/{volunteerId}/skills")
    public ResponseEntity<List<Skill>> getVolunteerSkills(@PathVariable Long volunteerId) {
        Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
        if (volunteerOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Volunteer volunteer = volunteerOpt.get();
        List<Skill> skills = new ArrayList<>(volunteer.getSkills());
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    /**
     * Adds a skill to a volunteer by their ID.
     *
     * @param volunteerId ID of the volunteer.
     * @param skillDto Data transfer object containing skill details.
     * @return Updated volunteer or HTTP 404 if not found.
     */
    @PostMapping("/volunteers/{volunteerId}/skills")
    @Transactional
    public ResponseEntity<Volunteer> addSkillToVolunteer(@PathVariable Long volunteerId, @RequestBody @Valid SkillDto skillDto) {
        Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
        if (volunteerOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Volunteer volunteer = volunteerOpt.get();
        Skill skill = skillService.findByName(skillDto.getName().toLowerCase()).orElse(null);

        if (skill == null) {
            skill = new Skill();
            skill.setName(skillDto.getName().toLowerCase());
            skillService.saveSkill(skill);
        }

        volunteer.getSkills().add(skill);
        userService.saveUser(volunteer);
        return new ResponseEntity<>(volunteer, HttpStatus.OK);
    }

    /**
     * Removes a skill from a volunteer.
     *
     * @param volunteerId ID of the volunteer.
     * @param skillId ID of the skill to remove.
     * @return Updated volunteer or HTTP 404 if not found.
     */
    @DeleteMapping("/volunteers/{volunteerId}/skills/{skillId}")
    @Transactional
    public ResponseEntity<Volunteer> removeSkillFromVolunteer(@PathVariable Long volunteerId, @PathVariable Long skillId) {
        Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
        if (volunteerOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Volunteer volunteer = volunteerOpt.get();
        Skill skill = skillService.findById(skillId).orElse(null);

        if (skill == null || !volunteer.getSkills().contains(skill)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        volunteer.getSkills().remove(skill);
        userService.saveUser(volunteer);
        return new ResponseEntity<>(volunteer, HttpStatus.OK);
    }

    // Task Skills APIs
    /**
     * Retrieves all skills required for a task.
     *
     * @param taskId ID of the task.
     * @return List of skills or HTTP 404 if task not found.
     */
    @GetMapping("/tasks/{taskId}/skills")
    public ResponseEntity<List<Skill>> getTaskSkills(@PathVariable Long taskId) {
        Optional<Task> taskOpt = taskService.findById(taskId);
        if (taskOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOpt.get();
        List<Skill> skills = new ArrayList<>(task.getSkills());
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    /**
     * Adds a skill to a task by its ID.
     *
     * @param taskId ID of the task.
     * @param skillDto Data transfer object containing skill details.
     * @return Updated task or HTTP 404 if task not found.
     */
    @PostMapping("/tasks/{taskId}/skills")
    @Transactional
    public ResponseEntity<Task> addSkillToTask(@PathVariable Long taskId, @RequestBody @Valid SkillDto skillDto) {
        Optional<Task> taskOpt = taskService.findById(taskId);
        if (taskOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOpt.get();
        Skill skill = skillService.findByName(skillDto.getName().toLowerCase()).orElse(null);

        if (skill == null) {
            skill = new Skill();
            skill.setName(skillDto.getName().toLowerCase());
            skillService.saveSkill(skill);
        }

        task.getSkills().add(skill);
        taskService.saveTask(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Removes a skill from a task.
     *
     * @param taskId ID of the task.
     * @param skillId ID of the skill to remove.
     * @return Updated task or HTTP 404 if not found.
     */
    @DeleteMapping("/tasks/{taskId}/skills/{skillId}")
    @Transactional
    public ResponseEntity<Task> removeSkillFromTask(@PathVariable Long taskId, @PathVariable Long skillId) {
        Optional<Task> taskOpt = taskService.findById(taskId);
        if (taskOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOpt.get();
        Skill skill = skillService.findById(skillId).orElse(null);

        if (skill == null || !task.getSkills().contains(skill)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        task.getSkills().remove(skill);
        taskService.saveTask(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
