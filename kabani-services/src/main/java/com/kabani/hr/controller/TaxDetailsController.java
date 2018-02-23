package com.kabani.hr.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kabani.hr.entity.EmployeeIncomeTaxDetailsMaster;
import com.kabani.hr.entity.UserAttendanceDetails;
import com.kabani.hr.repository.EmployeeIncomeTaxDetailsMasterRepository;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/tax") // This means URL's start with /demo (after Application path)
public class TaxDetailsController {
	private final Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private EmployeeIncomeTaxDetailsMasterRepository employeeIncomeTaxDetailsMasterRepository;

	@PostMapping(path = "/income/all")
	public @ResponseBody List<EmployeeIncomeTaxDetailsMaster> getAllEmployeesIncomeTax() {
		// This returns a JSON or XML with the users
		return employeeIncomeTaxDetailsMasterRepository.findAll();
	}

	@GetMapping(path = "/income/all/{month}/{year}")
	public @ResponseBody List<EmployeeIncomeTaxDetailsMaster> getAllIncomeTaxInYearMonth(@PathVariable int year, @PathVariable int month) {
		 
		// This returns a JSON or XML with the users
		return employeeIncomeTaxDetailsMasterRepository.getTaxForMonthYearForEmployee(month, year);
	}

	@PostMapping(path = "/income/addorupdate")
	public @ResponseBody boolean updateOrAddIncomeTax(@RequestBody EmployeeIncomeTaxDetailsMaster employeeIncomeTaxDetailsMaster) {
		try {
			employeeIncomeTaxDetailsMasterRepository.save(employeeIncomeTaxDetailsMaster);
			return true;
		} catch (Exception e) {
			logger.error("****Exception in updateOrAddEmployee() " + e.getMessage());
			return false;

		}
	}

	@PostMapping(path = "/income/delete")
	public @ResponseBody boolean deleteIncomeTax(@RequestBody EmployeeIncomeTaxDetailsMaster employeeIncomeTaxDetailsMaster) {
		try {
			employeeIncomeTaxDetailsMasterRepository.delete(employeeIncomeTaxDetailsMaster);
			return true;
		} catch (Exception e) {
			logger.error("****Exception in deleteEmployee() " + e.getMessage());
			return false;

		}
	}
	
	@PostMapping(path = "/upload")
	public @ResponseBody String[] handleFileUpload(@RequestParam("file") MultipartFile file)
			throws Exception {
		String[] response = { "" };
		InputStream is = null;
		BufferedReader bfReader = null; 
		ArrayList employeeDataList = new ArrayList();
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				is = new ByteArrayInputStream(bytes);
				bfReader = new BufferedReader(new InputStreamReader(is));
				bfReader.readLine();// it reads the first line so it removes the heading from the file
				String employeeAttendanceString = null;
				String[] employeeAttendance = null;
				// employeeAttendance = bfReader.readLine().split(",")) != null
				while (((employeeAttendanceString = bfReader.readLine()) != null)
						&& ((employeeAttendance = employeeAttendanceString.split("\\|")) != null)) {

					EmployeeIncomeTaxDetailsMaster employeeIncomeTaxDetailsMaster = new EmployeeIncomeTaxDetailsMaster();
					employeeIncomeTaxDetailsMaster.setEmployeeCode(employeeAttendance[0]);
					employeeIncomeTaxDetailsMaster.setEmployeeName(employeeAttendance[1]);
					if(null != employeeAttendance[2] && !"".equals(employeeAttendance[2])) {
						employeeIncomeTaxDetailsMaster.setMonthlySalry(Float.parseFloat(employeeAttendance[2]));
					}else {
						employeeIncomeTaxDetailsMaster.setMonthlySalry(0);
					}
					
					if(null != employeeAttendance[3] && !"".equals(employeeAttendance[3])) {
						employeeIncomeTaxDetailsMaster.setTaxAmount(Float.parseFloat(employeeAttendance[3]));
					}else {
						employeeIncomeTaxDetailsMaster.setTaxAmount(0);
					}
					
					
					employeeIncomeTaxDetailsMaster.setTaxForTheYear(Integer.parseInt(employeeAttendance[4]));
					employeeIncomeTaxDetailsMaster.setTaxForTheMonth(Integer.parseInt(employeeAttendance[5]));
					employeeIncomeTaxDetailsMaster.setStatus(employeeAttendance[6]);
					  
					employeeDataList.add(employeeIncomeTaxDetailsMaster);

				}
				employeeIncomeTaxDetailsMasterRepository.save(employeeDataList);
				response[0] = "The file " + file.getOriginalFilename() + "is uploaded successfully";

			} catch (Exception e) {
				logger.error("****Exception in handleFileUpload() " + e.getMessage());
				response[0] = "****Exception in handleFileUpload() " + e.getMessage();
				throw e;
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception ex) {

				}
			}
		} else {
			response[0] = "The file is empty";
		}
		return response;
	}

}
