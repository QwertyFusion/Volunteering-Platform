package com.example.volunteer_platform.controller.views;

import com.example.volunteer_platform.controller.TaskController;
import com.example.volunteer_platform.controller.TaskSignupController;
import com.example.volunteer_platform.controller.UserController;
import com.example.volunteer_platform.dto.TaskDto;
import com.example.volunteer_platform.enums.TaskStatus;
import com.example.volunteer_platform.model.Organization;
import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.service.TaskSignupService;
import com.example.volunteer_platform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class OrganizationViewsController {
    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskSignupService taskSignupService;

    @Autowired
    private TaskSignupController taskSignupController;

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @GetMapping("/o/current_tasks")
    public ModelAndView currentTasks(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("organization_current_tasks");

        Long organizationId = (Long) request.getSession().getAttribute("userId");

        if (organizationId == null) {
            mav.addObject("errorMessage", "Organization ID not found in session.");
            log.error("Organization ID not found in session.");
            return mav; // Return early if organizationId is not found
        }

        ResponseEntity<List<Task>> response = taskController.getOrganizationTasks(organizationId);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Task> tasks = response.getBody();

            // Filter tasks to keep only those with status AVAILABLE
            List<Task> availableTasks = tasks != null ?
                    tasks.stream()
                            .filter(task -> TaskStatus.AVAILABLE.equals(task.getStatus()))
                            .toList() :
                    new ArrayList<>();

            mav.addObject("tasks", availableTasks.toArray(new Task[0]));
            log.info("Available tasks fetched successfully: {}", availableTasks.size());
        } else {
            mav.addObject("errorMessage", "Unable to load tasks. Please try again later.");
            log.error("Failed to fetch tasks, status code: {}", response.getStatusCode());
        }

        return mav;
    }

    @GetMapping("/o/task/create")
    public ModelAndView createTask() {
        ModelAndView modelAndView = new ModelAndView("organization_task_creation");
        modelAndView.addObject("taskDto", new TaskDto());
        return modelAndView;
    }

    @GetMapping("/o/task/view")
    public ModelAndView viewTask(@RequestParam Long taskId, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        ResponseEntity<Task> response = taskController.getTaskById(taskId);

        if (response.getStatusCode().is2xxSuccessful()) {
            Task task = response.getBody();
            Long organizationId = (Long) request.getSession().getAttribute("userId");
            Optional<Organization> organizationOpt = userService.findOrganizationById(organizationId);

            if (organizationOpt.isPresent()) {
                Organization organization = organizationOpt.get();
                if (!organization.getTasks().contains(task)) {
                    // Redirect to current tasks if the task does not belong to the organization
                    mav.setViewName("redirect:/o/current_tasks");
                    return mav;
                }
            } else {
                // Handle case where organization is not found
                mav.addObject("errorMessage", "Organization not found.");
                log.error("Organization not found for ID: {}", organizationId);
                mav.setViewName("error"); // or any other view you want to show
                return mav;
            }

            int applicantsCount = taskSignupService.getTaskSignups(taskId).size();
            mav.addObject("applicantsCount", applicantsCount);
            mav.addObject("task", task);
            log.info("Task fetched successfully: {}", task);
            mav.setViewName("organization_task_view"); // Set the view name for successful case
        } else {
            mav.addObject("errorMessage", "Unable to load task details. Please try again later.");
            log.error("Failed to fetch task, status code: {}", response.getStatusCode());
            mav.setViewName("error"); // or any other view you want to show
        }

        return mav;
    }

    @GetMapping("/o/task/applicants")
    public ModelAndView viewTaskApplicants(@RequestParam Long taskId) {
        ModelAndView mav = new ModelAndView("organization_task_applicants");
        Task task = taskController.getTaskById(taskId).getBody();
        mav.addObject("task", task);
        ResponseEntity<List<TaskSignup>> response = taskSignupController.getTaskSignups(taskId);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<TaskSignup> taskSignups = response.getBody();
            mav.addObject("taskSignups", taskSignups != null ? taskSignups.toArray(new TaskSignup[0]) : new TaskSignup[0]); // Get volunteer details like for each loop taskSignup: taskSignups, then taskSignup.getVolunteer().getName()
        } else {
            mav.addObject("errorMessage", "Unable to load task details. Please try again later.");
            log.error("Failed to fetch task, status code: {}", response.getStatusCode());
        }

        return mav;
    }

    @GetMapping("/o/history")
    public ModelAndView tasksHistory(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("organization_task_history");
        Long organizationId = (Long) request.getSession().getAttribute("userId");
        ResponseEntity<List<Task>> response = taskController.getOrganizationTasks(organizationId);
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Task> tasks = response.getBody();

            mav.addObject("tasks", tasks.toArray(new Task[0]));
        } else {
            mav.addObject("errorMessage", "Unable to load tasks. Please try again later.");
            log.error("Failed to fetch tasks, status code: {}", response.getStatusCode());
        }
        return mav;
    }

    @GetMapping("/o/profile/edit")
    public ModelAndView profileSettings() {
        return new ModelAndView("organization_profile_settings");
    }

    @GetMapping("/o/profile")
    public ModelAndView profile(@RequestParam Long id) {
        ModelAndView mav = new ModelAndView("organization_profile");
        log.info("Fetching profile for orgId: {}", id);
        ResponseEntity<Organization> response = userController.getOrganizationById(id);

        if (response.getStatusCode().is2xxSuccessful()) {
            Organization org = response.getBody();
            mav.addObject("orgId", id);
            assert org != null;
            mav.addObject("orgName", org.getName());
            mav.addObject("orgEmail", org.getEmail());
            mav.addObject("orgAddress", org.getAddress());
            mav.addObject("orgWebsite", org.getWebsite());
            mav.addObject("orgPhone", org.getPhoneNumber());
        } else {
            mav.addObject("errorMessage", "Unable to load organization details. Please try again later.");
            log.error("Failed to fetch task, status code: {}", response.getStatusCode());
        }
        return mav;
    }
}