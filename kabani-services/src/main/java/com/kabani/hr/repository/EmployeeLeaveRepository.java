package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.EmployeeLeave;

public interface EmployeeLeaveRepository extends CrudRepository<EmployeeLeave, Long>{
	 
	List<EmployeeLeave> findAll(); 
	
	@Query(value = "select count(*), name,status from UserAttendanceDetails  group by name,status")
     public List<Object> findDistinctByNameO();
	
	@Query(value = "select count(*),employeeCode, name,status from UserAttendanceDetails where status='Present' and YEAR(date) = :year AND MONTH(date) = :month group by employeeCode,name,status")
    public List<Object[]> findDistinctPresentInMonthYear(@Param("month") int month,@Param("year") int year);
    
    @Query(value = "select count(*),employeeCode, name,status from UserAttendanceDetails where status='1/2Present' and  YEAR(dateOfHoliday) = :year AND MONTH(dateOfHoliday) = :month group by employeeCode,name,status")
    public List<Object[]> findDistinctHalfPresentInMonthYear(@Param("month") int month,@Param("year") int year);
    
    @Query(value = "select count(*),employeeCode, name,status from UserAttendanceDetails where status='Present On leave(CL)' and  YEAR(dateOfHoliday) = :year AND MONTH(dateOfHoliday) = :month group by employeeCode,name,status")
    public List<Object[]> findDistinctCasualLeaveInMonthYear(@Param("month") int month,@Param("year") int year);
	 
 }
