package com.kabani.hr.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.kabani.hr.helper.SalaryCalculator;

import javassist.bytecode.stackmap.BasicBlock.Catch;

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

	@PostMapping(path = "/generateSalary/{year}/{month}")
	public @ResponseBody List generateSalary(@PathVariable String year, @PathVariable String month) {
		return salaryCalculator.calculateSalaryOfEmployees(year, month);
	}

	@RequestMapping(value = "/getSalary", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List getSalary(@RequestParam String year, @RequestParam String month) {
		return salaryCalculator.getSalary(Integer.parseInt(year), Integer.parseInt(month));
	}

	@RequestMapping(value = "/getSalaryExcel", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<InputStreamResource> getSalaryExel(@RequestParam String year, @RequestParam String month) {
		salaryCalculator.genereteExcel(Integer.parseInt(year), Integer.parseInt(month));
		/*ClassLoader loader = getClass().getClassLoader();
		try {
			File file = new File(loader.getResource("ho_wps_template.xlsx").getFile());
			System.out.println("xxxxxxxxxxxxx--->>"+loader.getResource("ho_wps_template.xlsx").getFile());
			FileInputStream fsIP = new FileInputStream(file);
			return ResponseEntity.ok().contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(new InputStreamResource(fsIP));
		} catch (Exception e) {

		}*/
		return null;		
	}

}
