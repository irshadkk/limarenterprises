package com.kabani.hr.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kabani.hr.entity.EmployeeDetailsMaster;
import com.kabani.hr.repository.EmployeeDetailsMasterRepository;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/employeeupload") // This means URL's start with /demo (after Application path)
public class EmployeeBulkUploadController {
	 
	@Autowired
	private EmployeeDetailsMasterRepository employeeDetailsMasterRepository;
 

	@PostMapping(path = "/bulk")
	public @ResponseBody Iterable<EmployeeDetailsMaster> handleFileUpload(@RequestParam("file") MultipartFile file) {
		InputStream is = null;
		BufferedReader bfReader = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				is = new ByteArrayInputStream(bytes);
				bfReader = new BufferedReader(new InputStreamReader(is));
				bfReader.readLine();// it reads the first line so it removes the heading from the file
				String[] employeeDetailsArrFromFile = null;
				String employeeDetailsStringFromFile = null;
				ArrayList<EmployeeDetailsMaster> employeeDetailsMasterArrList=new ArrayList();
				while (((employeeDetailsStringFromFile = bfReader.readLine())!= null) && (employeeDetailsArrFromFile = employeeDetailsStringFromFile.split(",")) != null) {

					EmployeeDetailsMaster employeeDetailsMaster = new EmployeeDetailsMaster();
					employeeDetailsMaster.setEmployeeCode(employeeDetailsArrFromFile[0]);
					employeeDetailsMaster.setEmployeeBioDeviceCode(employeeDetailsArrFromFile[1]);
					employeeDetailsMaster.setEmployeeName(employeeDetailsArrFromFile[2]);
					employeeDetailsMaster.setBranch(employeeDetailsArrFromFile[3]); 
					employeeDetailsMaster.setDesignation(employeeDetailsArrFromFile[4]);
					employeeDetailsMaster.setDepartment(employeeDetailsArrFromFile[5]);
					employeeDetailsMaster.setEmployeeAge(Integer.parseInt(employeeDetailsArrFromFile[6]));
					employeeDetailsMaster.setEmployeeSex(employeeDetailsArrFromFile[7]);
					employeeDetailsMaster.setDateOfBirth(formatter.parse(parseDate(employeeDetailsArrFromFile[8],"-")));
					employeeDetailsMaster.setNameOfGuardian(employeeDetailsArrFromFile[9]);
					employeeDetailsMaster.setDesignationCode(employeeDetailsArrFromFile[10]);
					employeeDetailsMaster.setDateOfJoining(formatter.parse(parseDate(employeeDetailsArrFromFile[11],"-")));
					employeeDetailsMaster.setMobileNumber(employeeDetailsArrFromFile[12]);
					employeeDetailsMaster.setEmailId(employeeDetailsArrFromFile[13]);
					employeeDetailsMaster.setBankName(employeeDetailsArrFromFile[14]);
					employeeDetailsMaster.setIfscCode(employeeDetailsArrFromFile[15]);
					employeeDetailsMaster.setBankAccountNumber(employeeDetailsArrFromFile[16]);
					employeeDetailsMaster.setHra(Integer.parseInt(parseInput(employeeDetailsArrFromFile[17])));
					employeeDetailsMaster.setDa(Integer.parseInt(parseInput(employeeDetailsArrFromFile[18])));
					employeeDetailsMaster.setBasic(Integer.parseInt(parseInput(employeeDetailsArrFromFile[19])));
					employeeDetailsMaster.setSalary(Integer.parseInt(parseInput(employeeDetailsArrFromFile[20])));
					employeeDetailsMaster.setTotalCasualAlloted(Integer.parseInt(parseInput(employeeDetailsArrFromFile[21])));
					employeeDetailsMaster.setCasualLeavesTaken(Integer.parseInt(parseInput(employeeDetailsArrFromFile[22])));
					employeeDetailsMaster.setCasualLeavesRemaining(Integer.parseInt(parseInput(employeeDetailsArrFromFile[23])));
					employeeDetailsMaster.setCityCompensationAllowence(Integer.parseInt(parseInput(employeeDetailsArrFromFile[24]))); 
					employeeDetailsMasterArrList.add(employeeDetailsMaster);
					 

				}
				employeeDetailsMasterRepository.save(employeeDetailsMasterArrList);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception ex) {

				}
			}
		}
		return employeeDetailsMasterRepository.findAll();
	}
	public String parseDate(String inputDate,String seperator) { 
		String[] dateString=inputDate.split("/");
		return dateString[0]+seperator+dateString[1]+seperator+dateString[2];
		
	}
	public String parseInput(String inputString) {  
		return inputString.replaceAll("\"", "");
	 }

}
