package com.kabani.hr.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kabani.hr.entity.SalaryStatus;
import com.kabani.hr.helper.ExcelOutputServiceImpl;
import com.kabani.hr.helper.SalaryCalculator;
import com.kabani.hr.repository.SalaryStatusRepository;
import com.kabani.hr.repository.WpsRepository;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/salary") // This means URL's start with /demo (after
// Application path)
public class SalaryController {
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private SalaryCalculator salaryCalculator;
	
	@Autowired
	private SalaryStatusRepository salaryStatusRepository;
	@Autowired
	private WpsRepository wpsRepository;
	@Autowired
	private ExcelOutputServiceImpl excelOutputServiceImpl;

	@RequestMapping(value = "/salaryGenerated", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody int getSalaryStatus(@RequestParam String year, @RequestParam String month) throws Exception {
		int response = 0;
		try {
			response = salaryCalculator.hasSalaryGenerated(year, month);
		} catch (Exception e) {
			logger.error("****Exception in getSalaryStatus() " + e.getMessage());
			throw e;
		}
		return response;
	}

	@PostMapping(path = "/generateSalary/{year}/{month}")
	public @ResponseBody List generateSalary(@PathVariable String year, @PathVariable String month) throws Exception {
		List returnValue = new ArrayList<>();
		try {
			returnValue = salaryCalculator.calculateSalaryOfEmployees(year, month);
		} catch (Exception e) {
			logger.error("****Exception in generateSalary() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	@RequestMapping(value = "/getSalary", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List getSalary(@RequestParam String year, @RequestParam String month) throws Exception {
		List returnValue = new ArrayList<>();
		try {
			returnValue = salaryCalculator.getSalary(Integer.parseInt(year), Integer.parseInt(month));
		} catch (Exception e) {
			logger.error("****Exception in getSalary() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	@RequestMapping(value = "/getSalaryExcel", method = RequestMethod.GET)
	public ModelAndView getSalaryExel(@RequestParam String year, @RequestParam String month,
			HttpServletResponse response) throws Exception {
		try {
			salaryCalculator.genereteExcel(Integer.parseInt(year), Integer.parseInt(month), response);
		} catch (Exception e) {
			logger.error("****Exception in getSalaryExel() " + e.getMessage());
			throw e;
		}
		return null;
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ModelAndView downloadExcelOutputExl(HttpServletResponse response) throws Exception {
		try {
			excelOutputServiceImpl.createExcelOutputExcel(response);
		} catch (Exception e) {
			logger.error("****Exception in downloadExcelOutputExl() " + e.getMessage());
			throw e;
		}
		return null;
	}
	
	@RequestMapping(value = "/salaryStatusAll", method = RequestMethod.GET)
	public @ResponseBody List getSalaryStatusAll() throws Exception {
		 List<SalaryStatus> result;
		try {
			result= salaryStatusRepository.findAll();
		} catch (Exception e) {
			logger.error("****Exception in getSalaryStatusAll() " + e.getMessage());
			throw e;
		}
		return result;
	}
	@RequestMapping(value = "/salaryStatusRemove", method = RequestMethod.POST)
	public boolean salaryStatusRemove(@RequestBody SalaryStatus salaryStatus) throws Exception {
		try {
			salaryStatusRepository.delete(salaryStatus);
		} catch (Exception e) {
			logger.error("****Exception in getSalaryStatusAll() " + e.getMessage());
			throw e;
		}
		return true;
	}
	@RequestMapping(value = "/salaryStatusResetAll", method = RequestMethod.GET)
	public String salaryStatusRemove() throws Exception {
		String result="";
		try {
			salaryStatusRepository.resetAll();
			wpsRepository.resetAll();
		} catch (Exception e) {
			logger.error("****Exception in getSalaryStatusAll() " + e.getMessage());
			throw e;
		}
		return result;
	}
	
	 

}
