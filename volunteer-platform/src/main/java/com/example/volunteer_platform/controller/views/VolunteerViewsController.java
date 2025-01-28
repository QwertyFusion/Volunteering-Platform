package com.example.volunteer_platform.controller.views;

import com.example.volunteer_platform.controller.TaskController;
import com.example.volunteer_platform.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for rendering volunteer views.
 */
@Controller
@Slf4j
public class VolunteerViewsController {

    private final TaskController taskController;

    @Autowired  // Dependency injection using constructor
    public VolunteerViewsController(TaskController taskController) {
        this.taskController = taskController;
    }

    /**
     * Displays all available volunteer opportunities.
     */
    @GetMapping("/v/opportunities")
    public ModelAndView viewOpportunities() {
        ModelAndView mav = new ModelAndView("volunteer_opportunities");

        ResponseEntity<List<Task>> response = taskController.getAllTasks(); // Get tasks as List<Task>

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Task> tasks = response.getBody();
            mav.addObject("tasks", (tasks != null ? tasks.toArray(new Task[0]) : new Task[0])); // Convert to Task[]
            log.info("Tasks fetched successfully: {}", (tasks != null ? tasks.size() : 0));
        } else {
            mav.addObject("errorMessage", "Unable to load tasks. Please try again later.");
            log.error("Failed to fetch tasks, status code: {}", response.getStatusCode());
        }

        return mav;
    }

    /**
     * Displays the details of a specific task by ID.
     */
    @GetMapping("/v/tasks/{taskId}")
    public ModelAndView viewTaskDetails(@PathVariable Long taskId) {
        ModelAndView mav = new ModelAndView("volunteer_task_view");

        ResponseEntity<Task> response = taskController.getTaskById(taskId); // Fetch task by ID

        if (response.getStatusCode().is2xxSuccessful()) {
            Task task = response.getBody();
            mav.addObject("task", task);
            log.info("Task details fetched successfully for ID: {}", taskId);
        } else {
            mav.addObject("errorMessage", "The requested task could not be found.");
            log.error("Failed to fetch task ID: {}, status code: {}", taskId, response.getStatusCode());
        }

        return mav;
    }

    @GetMapping("/v/history")
    public ModelAndView tasksHistory() {
        return new ModelAndView("volunteer_history");
    }

    @GetMapping("/v/profile/edit")
    public ModelAndView profileSettings() {
        return new ModelAndView("volunteer_profile_settings");
    }

    @GetMapping("/v/profile")
    public ModelAndView profile() {
        return new ModelAndView("volunteer_profile");
    }
}
