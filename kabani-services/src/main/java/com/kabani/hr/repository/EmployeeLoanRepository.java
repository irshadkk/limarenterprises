package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.EmployeeLoan;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EmployeeLoanRepository extends CrudRepository<EmployeeLoan, Long> {
	/*@Query("SELECT ss FROM EmployeeLoan ss WHERE id IN :idList ")
	List<EmployeeLoan> getActiveLoanForMonth(@Param("idList") List<Integer> idList);*/

	@Query("SELECT ss FROM EmployeeLoan ss WHERE ss.status='active' and ss.employeeCode=:employeeCode and YEAR(availDate) = :year AND MONTH(availDate) = :month")
	List<EmployeeLoan> getActiveLoanForMonthForEmployee(@Param("employeeCode") String employeeCode,
			@Param("month") Integer month, @Param("year") Integer year);

	@Query("SELECT ss FROM EmployeeLoan ss WHERE  ss.employeeCode=:employeeCode and ss.id=:loanId")
	EmployeeLoan getLoanInfo(@Param("employeeCode") String employeeCode, @Param("loanId") Integer loanId);

	List<EmployeeLoan> findAll();

	@Query(value = "SELECT el,\r\n"
			+ "(SELECT count(*) FROM EmployeeLoanorAdvanceDeduction elad WHERE elad.loanId=el.id and status='paid') as paidCount ,\r\n"
			+ "(SELECT count(*) FROM EmployeeLoanorAdvanceDeduction elad2 WHERE elad2.loanId=el.id and status='unpaid') as unpaidCount \r\n"
			+ "FROM EmployeeLoan el WHERE id IN :idList")
	public List<Object[]> getActiveLoanForMonth(@Param("idList") List<Integer> idList);

}