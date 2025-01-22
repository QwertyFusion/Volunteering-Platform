package com.example.volunteer_platform.service;

import com.example.volunteer_platform.dto.OrganizationDto;
import com.example.volunteer_platform.dto.OrganizationPartialDto;
import com.example.volunteer_platform.dto.VolunteerDto;
import com.example.volunteer_platform.dto.VolunteerPartialDto;
import com.example.volunteer_platform.model.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser (User user);
    void saveVolunteer(VolunteerDto volunteerDTO);
    void saveOrganization(OrganizationDto orgDTO);
    List<User> getAllUsers();
    User findByEmail(String email);
    Optional<User> findUserById(Long id);
    void deleteUserById(Long id);
    List<Organization> getAllOrganizations();
    List<Volunteer> getAllVolunteers();
    Optional<Organization> findOrganizationById(Long id);
    Optional<Volunteer> findVolunteerById(Long id);
    Optional<Organization> updateOrganization(Long organizationId, OrganizationPartialDto updatedOrg);
    boolean deleteOrganizationById(Long organizationId);
    Optional<Volunteer> updateVolunteer(Long volunteerId, VolunteerPartialDto updatedVol);
    boolean deleteVolunteerById(Long volunteerId);
}