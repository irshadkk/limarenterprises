package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.EmployeeSalaryDetails;

public interface EmployeeSalaryDetailsRepository extends CrudRepository<EmployeeSalaryDetails, Long>{
	 
	
	@Query("SELECT employeeCode FROM EmployeeSalaryDetails WHERE employeeCode IN :ids")
    List<Object> findByIdsIn(@Param("ids") List<String> ids);
	
	@Query("SELECT total FROM EmployeeSalaryDetails WHERE employeeCode =:id")
    String findSalaryById(@Param("id") String id);

}
