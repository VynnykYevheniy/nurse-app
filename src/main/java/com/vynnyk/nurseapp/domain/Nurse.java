package com.vynnyk.nurseapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nurse extends User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany
	@JoinTable(
			name = "nurse_services",
			joinColumns = @JoinColumn(name = "nurse_id"),
			inverseJoinColumns = @JoinColumn(name = "service_id")
	)
	private List<MedicalService> services;
}