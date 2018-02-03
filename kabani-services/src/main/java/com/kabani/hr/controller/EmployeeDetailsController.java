package com.kabani.hr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kabani.hr.entity.EmployeeDetailsMaster;
import com.kabani.hr.repository.EmployeeDetailsMasterRepository;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/employee") // This means URL's start with /demo (after Application path)
public class EmployeeDetailsController {
	
	@Autowired
	private EmployeeDetailsMasterRepository employeeDetailsMasterRepository;
	
	@GetMapping(path = "/all")
	public @ResponseBody List<EmployeeDetailsMaster> getAllEmployees() {
		// This returns a JSON or XML with the users
		return employeeDetailsMasterRepository.findAll();
	}
	@PostMapping(path = "/addorupdate")
	public @ResponseBody boolean updateOrAddEmployee(@RequestBody EmployeeDetailsMaster employeeDetailsMaster) { 
		 try{
			 employeeDetailsMasterRepository.save(employeeDetailsMaster);
			 return true;
		 }catch(Exception e) {
			 System.out.println("--------------------------------------------");
			 System.out.println(e);
			 System.out.println("--------------------------------------------");
			 return false;
			 
		 }
	}
 
	
}
