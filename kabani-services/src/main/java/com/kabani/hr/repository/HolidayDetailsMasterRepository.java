package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kabani.hr.entity.HolidayDetailsMaster;

public interface HolidayDetailsMasterRepository extends CrudRepository<HolidayDetailsMaster, Long>{
	 List<HolidayDetailsMaster> findAll();
	 void delete(HolidayDetailsMaster entity);
	 @Query("SELECT count(*) FROM HolidayDetailsMaster WHERE YEAR(dateOfHoliday) = :year AND MONTH(dateOfHoliday) = :month")
	 int findCountOfHolidayByYearMonth(@Param("year") Integer year, @Param("month") Integer month);
	 
	 @Query("SELECT ss FROM HolidayDetailsMaster ss WHERE YEAR(ss.dateOfHoliday) = :year AND MONTH(ss.dateOfHoliday) = :month")
	 List<HolidayDetailsMaster> findHolidaysByYearMonth(@Param("year") Integer year, @Param("month") Integer month);
	  
	 
	 

}
