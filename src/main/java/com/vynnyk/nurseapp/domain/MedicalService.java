package com.vynnyk.nurseapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "medical_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalService {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;        // Наприклад: "Крапельниця"
	private String description; // Можливо, необов'язкове поле

	@ManyToOne
	private Task task; // Связь с задачей (Task)

	@ManyToMany(mappedBy = "services")
	private List<User> nurses;
}
