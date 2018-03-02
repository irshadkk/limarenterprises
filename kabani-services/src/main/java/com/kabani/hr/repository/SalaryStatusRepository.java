package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.SalaryStatus;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SalaryStatusRepository extends CrudRepository<SalaryStatus, Long> {
	@Query("SELECT count(*) FROM SalaryStatus ss WHERE ss.year=:year and ss.month=:month and ss.type=:type")
	int isSalaryGenerated(@Param("year") Integer year, @Param("month") Integer month, @Param("type") String type);
	
	List<SalaryStatus> findAll();
	
	@Query("SELECT ss FROM SalaryStatus ss WHERE ss.year=:year and ss.month=:month and ss.type=:type")
	List<SalaryStatus> salaryGeneratedForMonth(@Param("year") Integer year, @Param("month") Integer month, @Param("type") String type);
	
	@Query("SELECT ss FROM SalaryStatus ss WHERE  ss.type=:type ORDER BY year,month desc")
	List<SalaryStatus> getAllByType(@Param("type") String type);
	
	 

}