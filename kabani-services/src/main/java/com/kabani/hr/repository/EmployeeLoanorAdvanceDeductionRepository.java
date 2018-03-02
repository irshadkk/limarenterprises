package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.EmployeeLoanorAdvanceDeduction;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EmployeeLoanorAdvanceDeductionRepository extends CrudRepository<EmployeeLoanorAdvanceDeduction, Long> {
	@Query("SELECT ss FROM EmployeeLoanorAdvanceDeduction ss WHERE ss.type='loan' and ss.status='unpaid' and  YEAR(availDate) = :year AND MONTH(availDate) = :month")
	List<EmployeeLoanorAdvanceDeduction> getActiveLoanDeductionForMonth(@Param("month") Integer month,
			@Param("year") Integer year);

	@Query("SELECT ss FROM EmployeeLoanorAdvanceDeduction ss WHERE ss.type=:type and ss.status=:status and  YEAR(availDate) = :year AND MONTH(availDate) = :month")
	List<EmployeeLoanorAdvanceDeduction> getDeductionForMonth(@Param("month") Integer month,
			@Param("year") Integer year,@Param("status") String status,@Param("type") String type);

	
	@Query("SELECT ss FROM EmployeeLoanorAdvanceDeduction ss  WHERE ss.loanId=:loanId" )
	List<EmployeeLoanorAdvanceDeduction> getAllInstallmentForLoan(@Param("loanId") Integer loanId);
	
	@Query("SELECT ss FROM EmployeeLoanorAdvanceDeduction ss WHERE ss.type='advance' and ss.status='unpaid' and  YEAR(availDate) = :year AND MONTH(availDate) = :month")
	List<EmployeeLoanorAdvanceDeduction> getActiveAdvancesForMonth(@Param("month") Integer month,
			@Param("year") Integer year);
	
	@Query("SELECT ss FROM EmployeeLoanorAdvanceDeduction ss WHERE ss.type='advance' and  YEAR(availDate) = :year AND MONTH(availDate) = :month")
	List<EmployeeLoanorAdvanceDeduction> getAllActiveAdvancesForMonth(@Param("month") Integer month,
			@Param("year") Integer year);
	
	List<EmployeeLoanorAdvanceDeduction> findAll();

}