package com.kabani.hr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kabani.hr.helper.SalaryCalculator;

@CrossOrigin
@Controller // This means that this class is a Controller
 @RequestMapping(path = "/salary") // This means URL's start with /demo (after
// Application path)
public class SalaryController {
	 

	@Autowired
	private SalaryCalculator salaryCalculator;

	@RequestMapping(value = "/salaryGenerated", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody int getSalaryStatus(@RequestParam String year, @RequestParam String month) {
		return salaryCalculator.hasSalaryGenerated(year, month);
	}
	 

	@PostMapping(path = "/getSalary/{year}/{month}")
	public @ResponseBody List getSalary(@PathVariable String year, @PathVariable String month) {
		return salaryCalculator.calculateSalaryOfEmployees(year, month);
	}

}
