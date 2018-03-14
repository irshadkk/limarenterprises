package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kabani.hr.entity.EmployeeDetailsMaster;

public interface EmployeeDetailsMasterRepository extends CrudRepository<EmployeeDetailsMaster, Long>{
	 	
    List<EmployeeDetailsMaster> findAll();
	 
    @Query("select edm from EmployeeDetailsMaster edm where edm.employeeName LIKE :name||'%' and edm.branch LIKE :branch||'%' and edm.designation LIKE :designation||'%' and edm.department LIKE :department||'%'  ")
    List<EmployeeDetailsMaster> filterEmployees(@Param("name") String name,@Param("branch") String branch,@Param("department") String department,@Param("designation") String designation);
    
    @Transactional
    @Modifying
    @Query("UPDATE EmployeeDetailsMaster  SET notElibibleForWelfareFund=1 WHERE DATEDIFF(SYSDATE(), dateOfBirth)/365>=55")
    void updateWelfareFundEligiblity();
    
}
