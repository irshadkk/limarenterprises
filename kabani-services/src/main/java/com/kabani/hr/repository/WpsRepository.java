package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kabani.hr.entity.Wps;

public interface WpsRepository extends  CrudRepository <Wps, Long>{ 
	 
	
	 
	List<Wps> findAll();

}
