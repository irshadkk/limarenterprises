package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.EmployeeIncomeTaxDetailsMaster;
import com.kabani.hr.entity.EmployeeLoan;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EmployeeIncomeTaxDetailsMasterRepository extends CrudRepository<EmployeeIncomeTaxDetailsMaster, Long> {
	@Query("SELECT ss FROM EmployeeIncomeTaxDetailsMaster ss WHERE  ss.taxForTheYear = :year AND ss.taxForTheMonth = :month")
	List<EmployeeIncomeTaxDetailsMaster> getTaxForMonthYearForEmployee(@Param("month") Integer month, @Param("year") Integer year);
	
	 

	List<EmployeeIncomeTaxDetailsMaster> findAll();

}