package com.vynnyk.nurseapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;
	private String location;
	private LocalDateTime requestTime = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	private TaskStatus status = TaskStatus.PENDING;

	@ManyToOne
	private User client;

	@ManyToOne
	private User nurse;
}