package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.EmployeeIncentive;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EmployeeIncentiveRepository extends CrudRepository<EmployeeIncentive, Long> {

	@Query("SELECT ss FROM EmployeeIncentive ss WHERE YEAR(date) = :year AND MONTH(date) = :month")
	List<EmployeeIncentive> getIncentiveForMonth(@Param("month") Integer month, @Param("year") Integer year);


	List<EmployeeIncentive> findAll();



}