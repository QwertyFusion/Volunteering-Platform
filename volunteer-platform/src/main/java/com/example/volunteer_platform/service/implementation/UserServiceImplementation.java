package com.example.volunteer_platform.service.implementation;

import com.example.volunteer_platform.dto.OrganizationDto;
import com.example.volunteer_platform.dto.OrganizationPartialDto;
import com.example.volunteer_platform.dto.VolunteerDto;
import com.example.volunteer_platform.dto.VolunteerPartialDto;
import com.example.volunteer_platform.model.*;
import com.example.volunteer_platform.repository.OrganizationRepository;
import com.example.volunteer_platform.repository.SkillRepository;
import com.example.volunteer_platform.repository.TaskSignupRepository;
import com.example.volunteer_platform.repository.VolunteerRepository;
import com.example.volunteer_platform.service.TaskService;
import com.example.volunteer_platform.service.TaskSignupService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.volunteer_platform.repository.UserRepository;
import com.example.volunteer_platform.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * UserServiceImplementation provides methods to manage users, including volunteers and organizations.
 * This is an implementation of the UserService interface.
 */
@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private TaskSignupService taskSignupService;

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private TaskSignupRepository taskSignupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Autowire PasswordEncoder

    @Override
    public void saveUser (User user) {
        userRepository.save(user);
    }

    
    
    
   /** @Override
    public List<Task> getVolunteeringHistory(Long volunteerId) {
        return taskSignupRepository.findCompletedTasksByVolunteerId(volunteerId);
    }**/

    
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User  not found with email: " + email);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user instanceof Organization ? "ORGANIZATION" : "VOLUNTEER") // Assign roles dynamically
                .build();
    }
    
    @Override
    @Transactional
    public void saveVolunteer(VolunteerDto volunteerDTO) {
        try {
            Volunteer volunteer = new Volunteer();
            volunteer.setName(volunteerDTO.getName());
            volunteer.setEmail(volunteerDTO.getEmail());
            volunteer.setPassword(passwordEncoder.encode(volunteerDTO.getPassword()));
            volunteer.setPhoneNumber(volunteerDTO.getPhoneNumber());
            volunteer.setGender(volunteerDTO.getGender());
            saveUser (volunteer);
        } catch (Exception e) {
            System.out.println("Error saving volunteer: " + e.getMessage());
            throw new RuntimeException("Could not create volunteer: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void saveOrganization(OrganizationDto orgDTO) {
        try {
            Organization org = new Organization();
            org.setName(orgDTO.getName());
            org.setEmail(orgDTO.getEmail());
            org.setPassword(passwordEncoder.encode(orgDTO.getPassword()));
            org.setPhoneNumber(orgDTO.getPhoneNumber());
            org.setAddress(orgDTO.getAddress());
            org.setWebsite(orgDTO.getWebsite());
            saveUser (org);
        } catch (Exception e) {
            System.out.println("Error saving organization: " + e.getMessage());
            throw new RuntimeException("Could not create organization: " + e.getMessage());
        }
    }
    
   
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Override
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    @Override
    public Optional<Organization> findOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    @Override
    public Optional<Volunteer> findVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }
    
    @Override
    public Optional<Volunteer> findVolunteerByEmailOptional(String email) {
        return volunteerRepository.findByEmail(email); // Direct return
    }


    
    @Override
    @Transactional
    public Optional<Organization> updateOrganization(Long organizationId, OrganizationPartialDto updatedOrg) {
        try {
            Organization existingOrg = findOrganizationById(organizationId).orElse(null);
            if (existingOrg == null) {
                return Optional.empty();
            }

            existingOrg.setName(updatedOrg.getName() != null ? updatedOrg.getName() : existingOrg.getName());
            existingOrg.setEmail(updatedOrg.getEmail() != null ? updatedOrg.getEmail() : existingOrg.getEmail());
            existingOrg.setPassword(updatedOrg.getPassword() != null ? updatedOrg.getPassword() : existingOrg.getPassword());
            existingOrg.setPhoneNumber(updatedOrg.getPhoneNumber() != null ? updatedOrg.getPhoneNumber() : existingOrg.getPhoneNumber());
            existingOrg.setAddress(updatedOrg.getAddress() != null ? updatedOrg.getAddress() : existingOrg.getAddress());
            existingOrg.setWebsite(updatedOrg.getWebsite() != null ? updatedOrg.getWebsite() : existingOrg.getWebsite());
            
            
           
            
            saveUser (existingOrg);
            return Optional.of(existingOrg);
        } catch (Exception e) {
            System.out.println("Error updating organization: " + e.getMessage());
            throw new RuntimeException("Could not update organization: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteOrganizationById(Long organizationId) {
        try {
            Optional<Organization> organizationOpt = findOrganizationById(organizationId);
            if (organizationOpt.isEmpty()) {
                return false;
            }

            Organization organization = organizationOpt.get();
            List<Task> tasks = organization.getTasks();

            for (Task task : tasks) {
                List<TaskSignup> signups = taskSignupService.getTaskSignups(task.getId());

                for (TaskSignup signup : signups) {
                    taskSignupService.deleteById(signup.getSignupId());
                }

                taskService.deleteByTaskId(task.getId());
            }

            deleteUserById(organizationId);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting organization: " + e.getMessage());
            throw new RuntimeException("Could not delete organization: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<Volunteer> updateVolunteer(Long volunteerId, VolunteerPartialDto updatedVol) {
        try {
            Optional<Volunteer> optionalVol = findVolunteerById(volunteerId);
            if (optionalVol.isEmpty()) {
                return Optional.empty(); // Return empty Optional instead of ResponseEntity
            }

            Volunteer existingVol = optionalVol.get();

            // Update fields if provided
            if (updatedVol.getName() != null) existingVol.setName(updatedVol.getName());
            if (updatedVol.getEmail() != null) existingVol.setEmail(updatedVol.getEmail());
            if (updatedVol.getPhoneNumber() != null) existingVol.setPhoneNumber(updatedVol.getPhoneNumber());

            // Handle password update properly
            if (updatedVol.getPassword() != null && !updatedVol.getPassword().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(updatedVol.getPassword());
                existingVol.setPassword(hashedPassword);
            }

            // Update skills if provided
          /**  if (updatedVol.getSkillIds() != null && !updatedVol.getSkillIds().isEmpty()) {
                Set<Skill> updatedSkills = new HashSet<>();
                for (Long skillId : updatedVol.getSkillIds()) {
                    skillRepository.findById(skillId).ifPresent(updatedSkills::add);
                }
                existingVol.setSkills(updatedSkills);
            }**/

            // Save changes
            saveUser(existingVol);
            return Optional.of(existingVol);
        } catch (Exception e) {
            // Log the error
            // log.error("Error updating volunteer: {}", e.getMessage());
            return Optional.empty(); // Return empty Optional on failure
        }
    }

    
   
    @Override
    @Transactional
    public boolean deleteVolunteerById(Long volunteerId) {
        try {
            Optional<Volunteer> volunteerOpt = findVolunteerById(volunteerId);
            if (volunteerOpt.isEmpty()) {
                return false;
            }

            List<TaskSignup> signups = taskSignupService.getUserSignups(volunteerId);

            for (TaskSignup signup : signups) {
                taskSignupService.deleteById(signup.getSignupId());
            }

            deleteUserById(volunteerId);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting volunteer: " + e.getMessage());
            throw new RuntimeException("Could not delete volunteer: " + e.getMessage());
        }
    }


}