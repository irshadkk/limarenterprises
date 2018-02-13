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

import com.kabani.hr.entity.EmployeeManualAttendance;
import com.kabani.hr.repository.EmployeeManualAttendanceRepository;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/manualattendance") // This means URL's start with /demo (after
// Application path)
public class EmployeeManualAttendanceController {
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private EmployeeManualAttendanceRepository employeeManualAttendanceRepository;
	 

	@GetMapping(path = "/all")
	public @ResponseBody List<EmployeeManualAttendance> getAllEmployees() {
		return employeeManualAttendanceRepository.findAll();
	}
	@PostMapping(path = "/addorupdate")
	public @ResponseBody boolean updateOrAddEmployee(@RequestBody EmployeeManualAttendance employeeManualAttendance) { 
		 try{
			 employeeManualAttendanceRepository.save(employeeManualAttendance);
			 return true;
		 }catch(Exception e) {
			 logger.error("****Exception in updateOrAddEmployee() "+e.getMessage());
			 return false;
			 
		 }
	}

}
