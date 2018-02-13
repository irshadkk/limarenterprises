package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.SalaryStatus;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SalaryStatusRepository extends CrudRepository<SalaryStatus, Long> {
	@Query("SELECT count(*) FROM SalaryStatus ss WHERE ss.year=:year and ss.month=:month")
	int isSalaryGenerated(@Param("year") Integer year, @Param("month") Integer month);
	
	List<SalaryStatus> findAll();
	
	@Query(value = " truncat  table salary_status) p", nativeQuery = true)
	int resetAll();

}