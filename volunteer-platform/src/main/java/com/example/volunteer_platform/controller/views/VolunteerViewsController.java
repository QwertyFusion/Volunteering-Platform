package com.example.volunteer_platform.controller.views;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.volunteer_platform.controller.TaskController;
import com.example.volunteer_platform.controller.TaskSignupController;
import com.example.volunteer_platform.controller.UserController;
import com.example.volunteer_platform.dto.TaskSignupDto;
import com.example.volunteer_platform.dto.VolunteerPartialDto;
import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.service.TaskSignupService;

import lombok.extern.slf4j.Slf4j;

import com.example.volunteer_platform.controller.TaskController;
import com.example.volunteer_platform.controller.TaskSignupController;
import com.example.volunteer_platform.controller.UserController;
import com.example.volunteer_platform.dto.TaskSignupDto;
import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.model.Volunteer;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class VolunteerViewsController {

    private final TaskController taskController;
    private final TaskSignupController taskSignupController;
    private final UserController userController;
    private final TaskSignupService tasksignupservice;
    @Autowired
    public VolunteerViewsController(TaskController taskController, TaskSignupController taskSignupController, UserController userController,TaskSignupService tasksignupservice) {
        this.taskController = taskController;
        this.taskSignupController = taskSignupController;
        this.userController = userController;
		this.tasksignupservice = tasksignupservice;
    }


    @GetMapping("/v/opportunities")
    public ModelAndView viewOpportunities() {
        ModelAndView mav = new ModelAndView("volunteer_opportunities");
        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Task> tasks = response.getBody();
            mav.addObject("tasks", tasks != null ? tasks.toArray(new Task[0]) : new Task[0]);
            log.info("Tasks fetched successfully: {}", tasks != null ? tasks.size() : 0);
        } else {
            mav.addObject("errorMessage", "Unable to load tasks. Please try again later.");
            log.error("Failed to fetch tasks, status code: {}", response.getStatusCode());
        }

        return mav;
    }

    @GetMapping("/v/opportunities/{taskId}")
    public ModelAndView viewTaskDetails(@PathVariable Long taskId) {
        ModelAndView mav = new ModelAndView("volunteer_task_view");
        ResponseEntity<Task> response = taskController.getTaskById(taskId);

        if (response.getStatusCode().is2xxSuccessful()) {
            Task task = response.getBody();
            mav.addObject("task", task);

            mav.addObject("signups", tasksignupservice.getTaskSignups(taskId));
      
            int applicantsCount = tasksignupservice.getTaskSignups(taskId).size();

            mav.addObject("applicantsCount", applicantsCount);
            
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

    public ModelAndView profile(Principal principal) {
        String email = principal.getName();
        ResponseEntity<Volunteer> volunteerResponse = userController.findVolunteerByEmailOptional(email);
        ModelAndView modelAndView = new ModelAndView("volunteer_profile");

        if (volunteerResponse != null && volunteerResponse.getBody() != null) {
            modelAndView.addObject("volunteer", volunteerResponse.getBody());
            log.info("Profile fetched successfully for volunteer with email: {}", email);
        } else {
            modelAndView.addObject("errorMessage", "Profile not found!");
            log.error("Profile not found for volunteer with email: {}", email);
        }
        return modelAndView;
    }
  
    @GetMapping("/v/history")
    public ModelAndView tasksHistory() {
        return new ModelAndView("volunteer_history");
    }

    /**@GetMapping("/v/profile/edit")
    public ModelAndView profileSettings() {
        return new ModelAndView("volunteer_profile_settings");
    }**/
    @GetMapping("/v/profile/edit")
    public ModelAndView profileSettings(Principal principal) {
        String email = principal.getName();
        ResponseEntity<Volunteer> volunteerResponse = userController.findVolunteerByEmailOptional(email);
        ModelAndView modelAndView = new ModelAndView("volunteer_profile_settings");

        if (volunteerResponse != null && volunteerResponse.getBody() != null) {
            modelAndView.addObject("volunteer", volunteerResponse.getBody());
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

