package com.kabani.hr.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kabani.hr.helper.ExcelOutputServiceImpl;
import com.kabani.hr.helper.SalaryCalculator;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/salary") // This means URL's start with /demo (after
// Application path)
public class SalaryController {

	@Autowired
	private SalaryCalculator salaryCalculator;
	@Autowired
	private ExcelOutputServiceImpl excelOutputServiceImpl;
	

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

	@RequestMapping(value = "/getSalaryExcel", method = RequestMethod.GET)
	public ModelAndView getSalaryExel(@RequestParam String year, @RequestParam String month,HttpServletResponse response) {
		salaryCalculator.genereteExcel(Integer.parseInt(year), Integer.parseInt(month),response); 
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
	 @RequestMapping(value="/download", method=RequestMethod.GET)
	    public ModelAndView downloadExcelOutputExl(HttpServletResponse response){
	      
		 excelOutputServiceImpl.createExcelOutputExcel(response);
	       return null;
	    }

}
