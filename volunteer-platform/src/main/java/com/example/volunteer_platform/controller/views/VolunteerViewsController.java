package com.example.volunteer_platform.controller.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.volunteer_platform.model.Task;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j



public class VolunteerViewsController {
	
	 @Autowired
	    private RestTemplate restTemplate; // Inject RestTemplate

	    @GetMapping("/v/opportunities")
	    public ModelAndView viewOpportunities() {
	        String taskApiUrl = "http://localhost:8080/api/tasks"; // Replace with your Task API URL

	        ModelAndView mav = new ModelAndView("volunteer_opportunities");

	        try {
	            ResponseEntity<Task[]> response = restTemplate.getForEntity(taskApiUrl, Task[].class); // Fetch tasks as an array

	            // Handle status codes
	            if (response.getStatusCode() == HttpStatus.OK) {
	                Task[] tasks = response.getBody();
	                mav.addObject("tasks", tasks);
	                log.info("Tasks fetched successfully: {}", (tasks != null ? tasks.length : 0));
	            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
	                log.error("No tasks found (404).");
	                mav.addObject("errorMessage", "No tasks available currently.");
	            } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
	                log.error("Server error occurred (500).");
	                mav.addObject("errorMessage", "An error occurred while fetching tasks. Please try again later.");
	            }
	        } catch (Exception e) {
	            log.error("Exception occurred while fetching tasks: {}", e.getMessage(), e);
	            mav.addObject("errorMessage", "Unable to load tasks. Please check back later.");
	        }

	        return mav;
	    }
	    
	    
	   /** @GetMapping("/v//tasks/{id}/view")
	    public ModelAndView viewTaskDetails(@PathVariable Long id) {
	        String taskDetailsApiUrl = "http://localhost:8080/api/tasks/" + id; // Replace with your Task Details API URL

	        ModelAndView mav = new ModelAndView("volunteer_task_view");

	        try {
	            ResponseEntity<Task> response = restTemplate.getForEntity(taskDetailsApiUrl, Task.class); // Fetch specific task by ID

	            // Handle status codes
	            if (response.getStatusCode() == HttpStatus.OK) {
	                Task task = response.getBody();
	                mav.addObject("task", task);
	                log.info("Task details fetched successfully for ID: {}", id);
	            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
	                log.error("Task not found for ID: {} (404).", id);
	                mav.addObject("errorMessage", "The requested task could not be found.");
	            } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
	                log.error("Server error occurred while fetching task ID: {} (500).", id);
	                mav.addObject("errorMessage", "An error occurred while fetching the task. Please try again later.");
	            }
	        } catch (Exception e) {
	            log.error("Exception occurred while fetching task ID: {}: {}", id, e.getMessage(), e);
	            mav.addObject("errorMessage", "Unable to load the task details. Please check back later.");
	        }

	        return mav;
	    }
	
	**/

	
   

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
