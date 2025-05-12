package com.vynnyk.nurseapp.controller;

import com.vynnyk.nurseapp.domain.MedicalService;
import com.vynnyk.nurseapp.repository.MedicalServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/med-service")
@AllArgsConstructor
public class MedicalServiceController {
	private final MedicalServiceRepository medicalServiceRepository;

	@GetMapping
	public List<MedicalService> getAll() {
		return medicalServiceRepository.findAll();
	}

	@GetMapping("/filter")
	public List<MedicalService> getAllByLocationAndNurse(@RequestParam String city,
														 @RequestParam Long nurseId) {
		return medicalServiceRepository.findAllByCityAndNurseId(city, nurseId);
	}

	@GetMapping("/by-city")
	public List<MedicalService> getAllByCity(@RequestParam String city) {
		return medicalServiceRepository.findAllByCity(city);
	}

}
