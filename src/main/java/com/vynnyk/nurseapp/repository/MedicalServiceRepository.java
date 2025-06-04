package com.vynnyk.nurseapp.repository;

import com.vynnyk.nurseapp.domain.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {

	@Query("SELECT ms FROM MedicalService ms JOIN ms.nurses n WHERE n.id = :nurseId AND n.city = :city")
	List<MedicalService> findAllByCityAndNurseId(@Param("city") String city, @Param("nurseId") Long nurseId);

	@Query("SELECT DISTINCT ms FROM MedicalService ms JOIN ms.nurses n WHERE n.city = :city")
	List<MedicalService> findAllByCity(@Param("city") String city);
}

