package com.kabani.hr.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kabani.hr.entity.EmployeeDetailsMaster;
import com.kabani.hr.helper.EmployeeHelper;
import com.kabani.hr.helper.SalaryCalculator;
import com.kabani.hr.repository.EmployeeDetailsMasterRepository;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/employee") // This means URL's start with /demo (after Application path)
public class EmployeeDetailsController {
	private final Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private EmployeeDetailsMasterRepository employeeDetailsMasterRepository;
	
	@Autowired
	private EmployeeHelper employeeHelper;

	@GetMapping(path = "/all")
	public @ResponseBody List<EmployeeDetailsMaster> getAllEmployees() {
		return employeeDetailsMasterRepository.findAll();
	}

	@PostMapping(path = "/addorupdate")
	public @ResponseBody boolean updateOrAddEmployee(@RequestBody EmployeeDetailsMaster employeeDetailsMaster) {
		try {
			employeeDetailsMasterRepository.save(employeeDetailsMaster);
			return true;
		} catch (Exception e) {
			logger.error("****Exception in updateOrAddEmployee() " + e.getMessage());
			return false;

		}
	}

	@RequestMapping(value = "/filterEmployees", method = RequestMethod.GET)
	public ModelAndView filterEmployees(@RequestParam String filter,HttpServletResponse response) throws Exception {
		try {
			
			employeeHelper.genereteExcel(filter, response);
		} catch (Exception e) {
			logger.error("****Exception in getSalaryExel() " + e.getMessage());
			throw e;
		}
		return null;
	}

}
