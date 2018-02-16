package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.UserAttendanceDetails;

public interface UserAttendanceDetailsRepository extends CrudRepository<UserAttendanceDetails, Long>{
	@Query("SELECT name FROM UserAttendanceDetails")
	List<UserAttendanceDetails> findDistinctByName(); 
	
	@Query(value = "select count(*), name,status from UserAttendanceDetails  group by name,status")
     public List<Object> findDistinctByNameO();
	
	@Query(value = "select count(*),employeeCode, name,status from UserAttendanceDetails where status='Present' and YEAR(date) = :year AND MONTH(date) = :month group by employeeCode,name,status")
    public List<Object[]> findDistinctPresentInMonthYear(@Param("month") int month,@Param("year") int year);
    
    @Query(value = "select count(*),employeeCode, name,status from UserAttendanceDetails where status='1/2Present' and  YEAR(date) = :year AND MONTH(date) = :month group by employeeCode,name,status")
    public List<Object[]> findDistinctHalfPresentInMonthYear(@Param("month") int month,@Param("year") int year);
    
    @Query(value = "select count(*),employeeCode, name,status from UserAttendanceDetails where status='Present On leave(CL)' and  YEAR(date) = :year AND MONTH(date) = :month group by employeeCode,name,status")
    public List<Object[]> findDistinctCasualLeaveInMonthYear(@Param("month") int month,@Param("year") int year);
	
//	@Query(value = "select count(*), name,status from UserAttendanceDetails where status='1/2Present' group by name,status")
//    public List<Object[]> findDistinctHalfPresentByName();
	
//	@Query(value = " select max(dayscount) from (select distinct count(*)  as dayscount from user_attendance_details  where month = ?1 group by employee_name) p", nativeQuery = true)
//    public Object findNumberOfDaysInMonth(@Param("month") String month);
    @Query(value = "select uad  from UserAttendanceDetails  uad where  YEAR(uad.date) = :year AND MONTH(uad.date) = :month ")
	public List<UserAttendanceDetails> findAllAttendanceForMonth(@Param("month") int month, @Param("year") int year);

	 
	
	
	 
}
