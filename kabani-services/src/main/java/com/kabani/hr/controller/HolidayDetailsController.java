package com.kabani.hr.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kabani.hr.entity.EmployeeDetailsMaster;
import com.kabani.hr.entity.HolidayDetailsMaster;
import com.kabani.hr.repository.HolidayDetailsMasterRepository;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/holiday") // This means URL's start with /demo (after Application path)
public class HolidayDetailsController {
	private final Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private HolidayDetailsMasterRepository holidayDetailsMasterRepository;

	@PostMapping(path = "/all")
	public @ResponseBody List<HolidayDetailsMaster> getAllEmployees() {
		// This returns a JSON or XML with the users
		return holidayDetailsMasterRepository.findAll();
	}

	@GetMapping(path = "/all/{month}/{year}")
	public @ResponseBody int getAllHolidayInYearMonth() {
		// This returns a JSON or XML with the users
		return holidayDetailsMasterRepository.findCountOfHolidayByYearMonth();
	}

	@PostMapping(path = "/addorupdate")
	public @ResponseBody boolean updateOrAddEmployee(@RequestBody HolidayDetailsMaster holidayDetailsMaster) {
		try {
			holidayDetailsMasterRepository.save(holidayDetailsMaster);
			return true;
		} catch (Exception e) {
			logger.error("****Exception in updateOrAddEmployee() " + e.getMessage());
			return false;

		}
	}

	@PostMapping(path = "/delete")
	public @ResponseBody boolean deleteEmployee(@RequestBody HolidayDetailsMaster holidayDetailsMaster) {
		try {
			holidayDetailsMasterRepository.delete(holidayDetailsMaster);
			return true;
		} catch (Exception e) {
			logger.error("****Exception in deleteEmployee() " + e.getMessage());
			return false;

		}
	}

}
