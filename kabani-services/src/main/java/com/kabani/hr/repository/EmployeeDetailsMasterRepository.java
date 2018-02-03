package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.EmployeeDetailsMaster;

public interface EmployeeDetailsMasterRepository extends CrudRepository<EmployeeDetailsMaster, Long>{
	 
	
	
    List<EmployeeDetailsMaster> findAll();
	 

}
