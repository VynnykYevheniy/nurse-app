package com.vynnyk.nurseapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private String city;

	private String address;

	private LocalDateTime requestTime = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	private TaskStatus status = TaskStatus.PENDING;

	@ManyToOne
	private User client;

	@ManyToOne
	private User nurse;

	// Теперь связь OneToMany, так как одна задача может иметь несколько услуг
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MedicalService> services = new ArrayList<>();


	private LocalDate date;
	private LocalTime timeFrom;
	private LocalTime timeTo;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskFile> files = new ArrayList<>();
}