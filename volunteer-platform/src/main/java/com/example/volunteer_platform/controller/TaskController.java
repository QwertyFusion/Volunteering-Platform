package com.example.volunteer_platform.controller;
import java.util.List;

import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tasks")
@RestController
public class TaskController {

	@Autowired
	TaskService taskService;
	  

	    @PostMapping()
	    public Task createTask(@RequestBody Task task) {
	        return taskService.createTask(task);
	    }

	    @PutMapping("/update/{taskId}")
	    public Task updateTask(@PathVariable Integer taskId, @RequestBody Task taskDetails) {
	        return taskService.updateTask(taskId, taskDetails);
	    }

	    @DeleteMapping("/delete/{taskId}")
	    public String deleteTask(@PathVariable Integer taskId) {
	        taskService.deleteTask(taskId);
	        return "Task deleted successfully";
	    }

	    @GetMapping
	    public List<Task> getAllTasks() {
	        return taskService.getAllTasks();
	    }
}
