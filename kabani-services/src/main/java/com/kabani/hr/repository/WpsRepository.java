package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.Wps;

public interface WpsRepository extends  CrudRepository <Wps, Long>{ 
	 
	
	 
	List<Wps> findAll();
	
	@Query("SELECT ss FROM Wps ss WHERE ss.year=:year and ss.month=:month")
	List<Wps> findForCurrentMonth(@Param("year") Integer year, @Param("month") Integer month);
	
	@Query(value = " truncat  table wps) p", nativeQuery = true)
	List<Wps> resetAll();
	
	 
	
	

}
