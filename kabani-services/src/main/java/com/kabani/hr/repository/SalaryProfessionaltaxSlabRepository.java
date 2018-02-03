package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kabani.hr.entity.SalaryProfessionaltaxSlab;

public interface SalaryProfessionaltaxSlabRepository extends CrudRepository<SalaryProfessionaltaxSlab, Long>{
	
	List<SalaryProfessionaltaxSlab> findAll();

}
