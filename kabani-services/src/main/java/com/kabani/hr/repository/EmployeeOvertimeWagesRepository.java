package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.EmployeeOvertimeWages;

public interface EmployeeOvertimeWagesRepository extends CrudRepository<EmployeeOvertimeWages, Long>{
	 
	List<EmployeeOvertimeWages> findAll(); 
	
	@Query("SELECT ss FROM EmployeeOvertimeWages ss WHERE YEAR(availDate) = :year AND MONTH(availDate) = :month")
	List<EmployeeOvertimeWages> getOvertimeWagesForMonth(@Param("month") Integer month, @Param("year") Integer year);
	
	 }
