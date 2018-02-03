package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kabani.hr.entity.SalaryIncometaxSlab;

public interface SalaryIncometaxSlabRepository extends CrudRepository<SalaryIncometaxSlab, Long>{
	
	List<SalaryIncometaxSlab> findAll();

}
