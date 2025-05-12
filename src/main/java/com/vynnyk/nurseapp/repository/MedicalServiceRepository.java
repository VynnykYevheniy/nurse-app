package com.vynnyk.nurseapp.repository;

import com.vynnyk.nurseapp.domain.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {
	@Query("SELECT ms FROM MedicalService ms JOIN ms.nurses n WHERE n.id = :nurseId AND n.city = :city")
	List<MedicalService> findAllByCityAndNurseId(String city, Long nurseId);

	@Query("SELECT DISTINCT ms FROM MedicalService ms JOIN ms.nurses n WHERE n.city = :city")
	List<MedicalService> findAllByCity(String city);

}
