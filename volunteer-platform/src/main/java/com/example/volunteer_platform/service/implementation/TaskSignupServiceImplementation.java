package com.example.volunteer_platform.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.dto.ReminderStatusDTO;
import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.repository.TaskSignupRepository;
import com.example.volunteer_platform.service.TaskSignupService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TaskSignupServiceImpl provides methods to manage task signups in the system.
 * This is an implementation of the TaskSignupService interface.
 */
@Service
@RequiredArgsConstructor
public class TaskSignupServiceImplementation implements TaskSignupService {

    @Autowired
    private TaskSignupRepository taskSignupRepository;

    @Override
    public List<TaskSignup> getAllSignups() {
        return taskSignupRepository.findAll();
    }

    @Override
    public List<TaskSignup> getUserSignups(Long volunteerId) {
        return taskSignupRepository.findByVolunteerId(volunteerId);
    }

    @Override
    public List<TaskSignup> getTaskSignups(Long taskId) {
        return taskSignupRepository.findByTaskId(taskId);
    }

    @Override
    public Optional<TaskSignup> findById(Long signupId) {
        return taskSignupRepository.findById(signupId);
    }

    @Override
    public Optional<TaskSignup> findByTaskIdAndVolunteerId(Long taskId, Long id) {
        return taskSignupRepository.findByTaskIdAndVolunteerId(taskId, id);
    }

    @Override
    public void save(TaskSignup taskSignup) {
        taskSignupRepository.save(taskSignup);
    }

    @Override
    public void deleteById(Long signupId) {
        taskSignupRepository.deleteById(signupId);
    }

    @Override
    public List<ReminderStatusDTO> getReminderStatus() {
        List<TaskSignup> signups = taskSignupRepository.findAll();
        return signups.stream()
                .map(this::mapToReminderStatus)
                .collect(Collectors.toList());
    }

    @Override
    public ReminderStatusDTO getReminderStatusById(Long signupId) {
        TaskSignup signup = taskSignupRepository.findById(signupId)
                .orElseThrow();
        return mapToReminderStatus(signup);
    }

    private ReminderStatusDTO mapToReminderStatus(TaskSignup signup) {
        return ReminderStatusDTO.builder()
                .signupId(signup.getSignupId())
                .taskTitle(signup.getTask().getTitle())
                .volunteerEmail(signup.getVolunteer().getEmail())
                .eventDate(signup.getTask().getEventDate())
                .reminderSent(signup.isReminderSent())
                .build();
    }
}