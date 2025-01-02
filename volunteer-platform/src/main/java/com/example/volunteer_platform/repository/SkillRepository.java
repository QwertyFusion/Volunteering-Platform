package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    // You can define additional custom queries if required.
    // Example: Find a skill by its name.
    Skill findByName(String name);
}
