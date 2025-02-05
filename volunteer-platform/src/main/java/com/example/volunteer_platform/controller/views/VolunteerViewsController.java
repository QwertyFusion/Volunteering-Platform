package com.example.volunteer_platform.controller.views;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.volunteer_platform.enums.TaskStatus;
import com.example.volunteer_platform.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.volunteer_platform.controller.TaskController;
import com.example.volunteer_platform.controller.TaskSignupController;
import com.example.volunteer_platform.controller.UserController;
import com.example.volunteer_platform.dto.TaskSignupDto;
import com.example.volunteer_platform.dto.VolunteerPartialDto;
import com.example.volunteer_platform.service.TaskSignupService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class VolunteerViewsController {

    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskSignupController taskSignupController;

    @Autowired
    private UserController userController;

    @Autowired
    private TaskSignupService taskSignupService;

    @GetMapping("/v/opportunities")
    public ModelAndView viewOpportunities() {
        ModelAndView mav = new ModelAndView("volunteer_opportunities");
        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Task> tasks = response.getBody();
            List<Task> availableTasks = tasks != null ?
                    tasks.stream()
                            .filter(task -> TaskStatus.AVAILABLE.equals(task.getStatus()))
                            .toList() :
                    new ArrayList<>();

            // Create a map to hold organization names
            Map<Long, String> organizationNames = new HashMap<>();

            // Fetch organization names for each task
            for (Task task : availableTasks) {
                ResponseEntity<Organization> orgResponse = userController.getOrganizationById(task.getOrganizationId());
                if (orgResponse.getStatusCode().is2xxSuccessful() && orgResponse.getBody() != null) {
                    organizationNames.put(task.getOrganizationId(), orgResponse.getBody().getName());
                } else {
                    log.error("Organization not found with id: {}", task.getOrganizationId());
                    organizationNames.put(task.getOrganizationId(), "Unknown Organization"); // Fallback if organization not found
                }
            }

            mav.addObject("tasks", availableTasks.toArray(new Task[0]));
            mav.addObject("organizationNames", organizationNames); // Add the map to the model
            log.info("Tasks fetched successfully: {}", availableTasks.size());
        } else {
            mav.addObject("errorMessage", "Unable to load tasks. Please try again later.");
            log.error("Failed to fetch tasks, status code: {}", response.getStatusCode());
        }

        return mav;
    }

    @GetMapping("/v/opportunities/{taskId}")
    public ModelAndView viewTaskDetails(@PathVariable Long taskId, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("volunteer_task_view");
        ResponseEntity<Task> response = taskController.getTaskById(taskId);
        Long volunteerId = (Long) request.getSession().getAttribute("userId");

        if (response.getStatusCode().is2xxSuccessful()) {
            Task task = response.getBody();
            mav.addObject("task", task);

            mav.addObject("signups", taskSignupService.getTaskSignups(taskId));
      
            int applicantsCount = taskSignupService.getTaskSignups(taskId).size();

            mav.addObject("applicantsCount", applicantsCount);

            mav.addObject("alreadySignedUp", taskSignupController.isVolunteerSignedUp(taskId, volunteerId).getBody());
            
            log.info("Task details fetched successfully for ID: {}", taskId);
        } else {
            mav.addObject("errorMessage", "The requested task could not be found.");
            log.error("Failed to fetch task ID: {}, status code: {}", taskId, response.getStatusCode());
        }

        return mav;
    }

    @PostMapping("/v/opportunities/{taskId}/apply")
    public String applyForTask(@PathVariable Long taskId, Principal principal, Model model) {
        ResponseEntity<Task> taskResponse = taskController.getTaskById(taskId);
        if (taskResponse.getStatusCode() != HttpStatus.OK || taskResponse.getBody() == null) {
            log.error("Task not found with ID: {}", taskId);
            return "redirect:/v/opportunities?error=taskNotFound";
        }
        Task task = taskResponse.getBody();

        String email = principal.getName();
        ResponseEntity<Volunteer> volunteerResponse = userController.findVolunteerByEmailOptional(email);
        if (volunteerResponse.getStatusCode() != HttpStatus.OK || volunteerResponse.getBody() == null) {
            log.error("Volunteer not found with email: {}", email);
            return "redirect:/v/opportunities?error=volunteerNotFound";
        }
        Volunteer volunteer = volunteerResponse.getBody();

        ResponseEntity<Boolean> isSignedUpResponse = taskSignupController.isVolunteerSignedUp(taskId, volunteer.getId());
        boolean alreadySignedUp = isSignedUpResponse.getBody() != null && isSignedUpResponse.getBody();
        if (alreadySignedUp) {
            log.warn("Volunteer {} already signed up for task {}", volunteer.getId(), taskId);
            model.addAttribute("alreadySignedUp", alreadySignedUp);
            return "redirect:/v/opportunities/" + taskId + "?error=alreadySignedUp";
        }

        TaskSignupDto taskSignupDto = TaskSignupDto.builder()
                .taskId(task.getId())
                .volunteerId(volunteer.getId())
                .build();

        ResponseEntity<TaskSignup> response = taskSignupController.signUpForTask(taskSignupDto);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            log.info("Volunteer {} successfully signed up for task {}", volunteer.getId(), taskId);
            return "redirect:/v/opportunities/" + taskId + "?success=taskApplied";
        } else {
            log.error("Failed to sign up volunteer {} for task {}", volunteer.getId(), taskId);
            return "redirect:/v/opportunities?error=signupFailed";
        }
    }

    @DeleteMapping("/v/opportunities/{taskId}/cancel")
    public String cancelSignup(@PathVariable Long taskId, Principal principal, Model model) {
        String email = principal.getName();
        ResponseEntity<Volunteer> volunteerResponse = userController.findVolunteerByEmailOptional(email);
        if (volunteerResponse.getStatusCode() != HttpStatus.OK || volunteerResponse.getBody() == null) {
            log.error("Volunteer not found with email: {}", email);
            model.addAttribute("error", "Volunteer not found");
            return "redirect:/v/opportunities/" + taskId;
        }
        Volunteer volunteer = volunteerResponse.getBody();

        ResponseEntity<Void> cancelResponse = taskSignupController.cancelSignup(taskId, volunteer.getId());
        if (cancelResponse.getStatusCode() == HttpStatus.OK) {
            log.info("Volunteer {} successfully canceled signup for task {}", volunteer.getId(), taskId);
            model.addAttribute("success", "Cancellation successful!");
        } else {
            log.error("Failed to cancel signup for volunteer {} and task {}", volunteer.getId(), taskId);
            model.addAttribute("error", "Cancellation failed.");
        }

        return "redirect:/v/opportunities/" + taskId;
    }

    @GetMapping("/v/profile")
    public ModelAndView profile(@RequestParam Long id) {
        ModelAndView mav = new ModelAndView("volunteer_profile");
        log.info("Fetching profile for volId: {}", id);
        ResponseEntity<Volunteer> response = userController.getVolunteerById(id);

        if (response.getStatusCode().is2xxSuccessful()) {
            Volunteer volunteer = response.getBody();
            mav.addObject("volId", id);
            assert volunteer != null;
            mav.addObject("volName", volunteer.getName());
            mav.addObject("volEmail", volunteer.getEmail());
            mav.addObject("volGender", volunteer.getGender());
            mav.addObject("volSkills", volunteer.getSkills());
            mav.addObject("volPhone", volunteer.getPhoneNumber());
        } else {
            mav.addObject("errorMessage", "Unable to load volunteer details. Please try again later.");
            log.error("Failed to fetch task, status code: {}", response.getStatusCode());
        }
        return mav;
    }
  
    @GetMapping("/v/history")
    public ModelAndView tasksHistory(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("volunteer_history");
        Long volunteerId = (Long) request.getSession().getAttribute("userId");
        ResponseEntity<List<TaskSignup>> response = taskSignupController.getUserSignups(volunteerId);
        if (response.getStatusCode().is2xxSuccessful()) {
            List<TaskSignup> taskSignups = response.getBody();
            Map<Long, Task> taskMap = new HashMap<>();
            if (taskSignups != null) {
                for (TaskSignup taskSignup : taskSignups) {
                    ResponseEntity<Task> taskResponse = taskController.getTaskById(taskSignup.getTask().getId());
                    if (taskResponse.getStatusCode().is2xxSuccessful() && taskResponse.getBody() != null) {
                        taskMap.put(taskSignup.getTask().getId(), taskResponse.getBody());
                    } else {
                        log.error("Task not found with id: {}", taskSignup.getTask().getId());
                    }
                }
            }

            mav.addObject("tasks", taskMap);
            mav.addObject("taskSignups", taskSignups.toArray());
        } else {
            mav.addObject("errorMessage", "Unable to load tasks. Please try again later.");
            log.error("Failed to fetch tasks, status code: {}", response.getStatusCode());
        }
        return mav;
    }

    @GetMapping("/v/profile/edit")
    public ModelAndView profileSettings(Principal principal) {
        String email = principal.getName();
        ResponseEntity<Volunteer> volunteerResponse = userController.findVolunteerByEmailOptional(email);
        ModelAndView modelAndView = new ModelAndView("volunteer_profile_settings");

        StringBuilder skillsString = new StringBuilder();
        if (volunteerResponse != null && volunteerResponse.getBody() != null) {
            modelAndView.addObject("volunteer", volunteerResponse.getBody());
            for (Skill skill : volunteerResponse.getBody().getSkills()) {
                skillsString.append(skill.getName());
                skillsString.append(", ");
            }
            modelAndView.addObject("skills", skillsString.toString());
        } else {
            modelAndView.addObject("errorMessage", "Profile not found!");
        }
        return modelAndView;
    }

    @PostMapping("/v/profile/update")
    public String updateVolunteerProfile(@ModelAttribute VolunteerPartialDto volunteerDto, 
                                         Principal principal, RedirectAttributes redirectAttributes) {
        String email = principal.getName();
        ResponseEntity<Volunteer> volunteerResponse = userController.findVolunteerByEmailOptional(email);

        if (volunteerResponse.getStatusCode() != HttpStatus.OK || volunteerResponse.getBody() == null) {
            redirectAttributes.addFlashAttribute("error", "Volunteer not found!");
            return "redirect:/v/profile/edit";
        }

        Volunteer volunteer = volunteerResponse.getBody();

        ResponseEntity<Volunteer> updatedVolunteerResponse = userController.updateVolunteerById(volunteer.getId(), volunteerDto);
        
        if (updatedVolunteerResponse.getStatusCode() == HttpStatus.OK && updatedVolunteerResponse.getBody() != null) {
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Profile update failed!");
        }

        return "redirect:/v/profile";
    }

   /** @PostMapping("/v/profile/delete")
    public String deleteVolunteerProfile(Principal principal, RedirectAttributes redirectAttributes) {
        String email = principal.getName();
        ResponseEntity<Volunteer> volunteerResponse = userController.findVolunteerByEmailOptional(email);

        if (volunteerResponse.getStatusCode() != HttpStatus.OK || volunteerResponse.getBody() == null) {
            redirectAttributes.addFlashAttribute("error", "Volunteer not found!");
            return "redirect:/v/profile/edit";
        }

        Volunteer volunteer = volunteerResponse.getBody();
        boolean isDeleted = userController.deleteVolunteerById(volunteer.getId());

        if (isDeleted) {
            redirectAttributes.addFlashAttribute("success", "Account deleted successfully!");
            return "redirect:/logout"; // Redirect to logout or homepage
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete account!");
            return "redirect:/v/profile/edit";
        }
    }
**/
}

