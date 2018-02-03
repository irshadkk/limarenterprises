package com.kabani.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kabani.hr.entity.HolidayDetailsMaster;

public interface HolidayDetailsMasterRepository extends CrudRepository<HolidayDetailsMaster, Long>{
	 List<HolidayDetailsMaster> findAll();
	 void delete(HolidayDetailsMaster entity);
	 @Query("SELECT count(*) FROM HolidayDetailsMaster WHERE YEAR(dateOfHoliday) = 2018 AND MONTH(dateOfHoliday) = 5")
	 int findCountOfHolidayByYearMonth();
	 
	 

}
