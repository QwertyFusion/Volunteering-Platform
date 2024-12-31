package com.example.volunteer_platform.service;
import com.example.volunteer_platform.model.Task;
import com.example.volunteer_platform.repository.TaskRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepository;
	
	
	    public Task createTask(Task task) {
	        return taskRepository.save(task);
	    }

	    public Task updateTask(Integer taskId, Task taskDetails) {
	        Task task = taskRepository.findById(taskId)
	                                   .orElseThrow(() -> new RuntimeException("Task not found"));
	        task.setTitle(taskDetails.getTitle());
	        task.setDescription(taskDetails.getDescription());
	        task.setLocation(taskDetails.getLocation());
	        task.setSkills(taskDetails.getSkills());
	        task.setDate(taskDetails.getDate());
	        task.setTime(taskDetails.getTime());
	        task.setStatus(taskDetails.getStatus());
	        return taskRepository.save(task);
	    }

	    public void deleteTask(Integer taskId) {
	        taskRepository.deleteById(taskId);
	    }

	    public List<Task> getAllTasks() {
	        return taskRepository.findAll();
	    }
	}

	

