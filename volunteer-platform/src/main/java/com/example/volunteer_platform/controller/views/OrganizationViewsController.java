package com.example.volunteer_platform.controller.views;

import com.example.volunteer_platform.controller.TaskController;
import com.example.volunteer_platform.controller.TaskSignupController;
import com.example.volunteer_platform.dto.TaskDto;
import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.model.TaskSignup;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.service.TaskSignupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
public class OrganizationViewsController {
    @Autowired
    private TaskController taskController;

    @Autowired
    private TaskSignupService taskSignupService;

    @Autowired
    private TaskSignupController taskSignupController;

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
            mav.addObject("tasks", tasks != null ? tasks.toArray(new Task[0]) : new Task[0]);
            log.info("Tasks fetched successfully: {}", tasks != null ? tasks.size() : 0);
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
    public ModelAndView viewTask(@RequestParam Long taskId) {
        ModelAndView mav = new ModelAndView("organization_task_view");
        ResponseEntity<Task> response = taskController.getTaskById(taskId);

        if (response.getStatusCode().is2xxSuccessful()) {
            int applicantsCount = taskSignupService.getTaskSignups(taskId).size();
            mav.addObject("applicantsCount", applicantsCount);
            Task task = response.getBody();
            mav.addObject("task", task);
            log.info("Task fetched successfully: {}", task);
        } else {
            mav.addObject("errorMessage", "Unable to load task details. Please try again later.");
            log.error("Failed to fetch task, status code: {}", response.getStatusCode());
        }

        return mav;
    }

    @GetMapping("/o/task/applicants")
    public ModelAndView viewTaskApplicants(@RequestParam Long taskId) {
        ModelAndView mav = new ModelAndView("organization_task_applicants");
        ResponseEntity<List<TaskSignup>> response = taskSignupController.getTaskSignups(taskId);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<TaskSignup> taskSignups = response.getBody();
            mav.addObject("task", taskSignups != null ? taskSignups.toArray(new TaskSignup[0]) : new TaskSignup[0]); // Get volunteer details like for each loop taskSignup: taskSignups, then taskSignup.getVolunteer().getName()
        } else {
            mav.addObject("errorMessage", "Unable to load task details. Please try again later.");
            log.error("Failed to fetch task, status code: {}", response.getStatusCode());
        }

        return mav;
    }

    @GetMapping("/o/history")
    public ModelAndView tasksHistory() {
        return new ModelAndView("organization_task_history");
    }

    @GetMapping("/o/profile/edit")
    public ModelAndView profileSettings() {
        return new ModelAndView("organization_profile_settings");
    }

    @GetMapping("/o/profile")
    public ModelAndView profile() {
        return new ModelAndView("organization_profile");
    }
}