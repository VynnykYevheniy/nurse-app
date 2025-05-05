package com.vynnyk.nurseapp.controller;

import com.vynnyk.nurseapp.domain.Task;
import com.vynnyk.nurseapp.domain.TaskStatus;
import com.vynnyk.nurseapp.domain.User;
import com.vynnyk.nurseapp.repository.TaskRepository;
import com.vynnyk.nurseapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@AllArgsConstructor
public class TaskController {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	@PostMapping
	@PreAuthorize("hasRole('CLIENT')")
	public Task createRequest(@RequestBody Task task, Principal principal) {
		User client = userRepository.findByUsername(principal.getName()).orElseThrow();
		task.setClient(client);
		task.setStatus(TaskStatus.PENDING);
		return taskRepository.save(task);
	}

	@GetMapping("/available")
	@PreAuthorize("hasRole('NURSE')")
	public List<Task> getAvailableTasks(Principal principal) {
		return taskRepository.findByNurseIsNullAndStatus(TaskStatus.PENDING);
	}

	@PostMapping("/{id}/assign")
	@PreAuthorize("hasRole('NURSE')")
	public Task assignToMe(@PathVariable Long id, Principal principal) {
		Task task = taskRepository.findById(id).orElseThrow();
		if (task.getNurse() != null) throw new RuntimeException("Already assigned");

		User nurse = userRepository.findByUsername(principal.getName()).orElseThrow();
		task.setNurse(nurse);
		task.setStatus(TaskStatus.ACCEPTED);
		return taskRepository.save(task);
	}

	@PostMapping("/{id}/complete")
	@PreAuthorize("hasRole('NURSE')")
	public Task completeTask(@PathVariable Long id, Principal principal) {
		Task task = taskRepository.findById(id).orElseThrow();
		User nurse = userRepository.findByUsername(principal.getName()).orElseThrow();

		if (!nurse.equals(task.getNurse())) {
			throw new RuntimeException("You are not assigned to this task");
		}

		task.setStatus(TaskStatus.COMPLETED);
		return taskRepository.save(task);
	}

	@PostMapping("/{id}/cancel-by-nurse")
	@PreAuthorize("hasRole('NURSE')")
	public Task cancelByNurse(@PathVariable Long id, Principal principal) {
		Task task = taskRepository.findById(id).orElseThrow();
		User nurse = userRepository.findByUsername(principal.getName()).orElseThrow();

		if (!nurse.equals(task.getNurse())) {
			throw new RuntimeException("You are not assigned to this task");
		}

		// Освобождаем задачу
		task.setNurse(null);
		task.setStatus(TaskStatus.PENDING);
		return taskRepository.save(task);
	}

	@PostMapping("/{id}/cancel-by-client")
	@PreAuthorize("hasRole('CLIENT')")
	public Task cancelByClient(@PathVariable Long id, Principal principal) {
		Task task = taskRepository.findById(id).orElseThrow();
		User client = userRepository.findByUsername(principal.getName()).orElseThrow();

		if (!client.equals(task.getClient())) {
			throw new RuntimeException("This task does not belong to you");
		}

		task.setStatus(TaskStatus.CANCELLED);
		return taskRepository.save(task);
	}

	@GetMapping("/assigned")
	@PreAuthorize("hasRole('NURSE')")
	public List<Task> getAssignedTasks(Principal principal) {
		User nurse = userRepository.findByUsername(principal.getName()).orElseThrow();
		return taskRepository.findByNurse(nurse);
	}

	@GetMapping("/created")
	@PreAuthorize("hasRole('CLIENT')")
	public List<Task> getCreatedTasks(Principal principal) {
		User client = userRepository.findByUsername(principal.getName()).orElseThrow();
		return taskRepository.findByClient(client);
	}
}