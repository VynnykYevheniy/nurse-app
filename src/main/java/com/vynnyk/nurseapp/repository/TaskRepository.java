package com.vynnyk.nurseapp.repository;

import com.vynnyk.nurseapp.domain.Task;
import com.vynnyk.nurseapp.domain.TaskStatus;
import com.vynnyk.nurseapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByNurseIsNullAndStatus(TaskStatus status); // для медсестры

	List<Task> findByClient(User client); // для клиента

	List<Task> findByNurse(User nurse); // для медсестры, кто принял
}