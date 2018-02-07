package com.kabani.hr.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private final Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private EmployeeDetailsMasterRepository employeeDetailsMasterRepository;

	@PostMapping(path = "/bulk")
	public @ResponseBody Iterable<EmployeeDetailsMaster> handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
		InputStream is = null;
		BufferedReader bfReader = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				is = new ByteArrayInputStream(bytes);
				bfReader = new BufferedReader(new InputStreamReader(is));
				bfReader.readLine();// it reads the first line so it removes the heading from the file
				String[] employeeDetailsArrFromFile = null;
				String employeeDetailsStringFromFile = null;
				ArrayList<EmployeeDetailsMaster> employeeDetailsMasterArrList = new ArrayList();
				while (((employeeDetailsStringFromFile = bfReader.readLine()) != null)
						&& (employeeDetailsArrFromFile = employeeDetailsStringFromFile.split("\\|")) != null) {

					EmployeeDetailsMaster employeeDetailsMaster = new EmployeeDetailsMaster();
					if ((null != employeeDetailsArrFromFile[0]) && ("" != employeeDetailsArrFromFile[0])) {
						employeeDetailsMaster.setEmployeeName(employeeDetailsArrFromFile[0]);
						
					} else {
						employeeDetailsMaster.setEmployeeName("");
					}
					if ((null != employeeDetailsArrFromFile[2]) && ("" != employeeDetailsArrFromFile[2])) {
						employeeDetailsMaster.setEmployeeBioDeviceCode(parseInput(employeeDetailsArrFromFile[2]));

					} else {
						break;
					}
					if ((null != employeeDetailsArrFromFile[1]) && ("" != employeeDetailsArrFromFile[1])) {
						employeeDetailsMaster.setEmployeeCode(employeeDetailsArrFromFile[1]);


					} else {
						employeeDetailsMaster.setEmployeeCode("");
					}
					if ((null != employeeDetailsArrFromFile[3]) && ("" != employeeDetailsArrFromFile[3])) {
						employeeDetailsMaster.setBranch(employeeDetailsArrFromFile[3]);


					} else {
						employeeDetailsMaster.setBranch("");
					}
					if ((null != employeeDetailsArrFromFile[4]) && ("" != employeeDetailsArrFromFile[4])) {
						employeeDetailsMaster.setDesignation(employeeDetailsArrFromFile[4]);


					} else {
						employeeDetailsMaster.setDesignation("");

					}
					if ((null != employeeDetailsArrFromFile[5]) && ("" != employeeDetailsArrFromFile[5])) {
						employeeDetailsMaster.setDepartment(employeeDetailsArrFromFile[5]);
					} else {
						employeeDetailsMaster.setDepartment("");
					}
					if ((null != employeeDetailsArrFromFile[6]) && (!employeeDetailsArrFromFile[6].equals("")) && (!parseInput(employeeDetailsArrFromFile[6]).equals(""))) {
						employeeDetailsMaster.setEmployeeAge(Float.parseFloat(parseInput(employeeDetailsArrFromFile[6])));
						
					} else {
						employeeDetailsMaster.setEmployeeAge(0);
					}
					if ((null != employeeDetailsArrFromFile[7]) && ("" != employeeDetailsArrFromFile[7])) {
						employeeDetailsMaster.setEmployeeSex(employeeDetailsArrFromFile[7]);
						
					} else {
						employeeDetailsMaster.setEmployeeSex("");
					}
					if ((null != employeeDetailsArrFromFile[8]) && ("" != employeeDetailsArrFromFile[8])) {
						employeeDetailsMaster.setDateOfBirth(formatter.parse(parseDate(employeeDetailsArrFromFile[8], "-")));
						
					} else {
						employeeDetailsMaster.setDateOfBirth(formatter.parse("01-01-1900"));
					}
					if ((null != employeeDetailsArrFromFile[9]) && ("" != employeeDetailsArrFromFile[9])) {
						employeeDetailsMaster.setNameOfGuardian(employeeDetailsArrFromFile[9]);
						
					} else {
						employeeDetailsMaster.setNameOfGuardian("");
					}
					if ((null != employeeDetailsArrFromFile[10]) && (!employeeDetailsArrFromFile[10].equals("")) && (!parseInput(employeeDetailsArrFromFile[10]).equals(""))) {
						employeeDetailsMaster.setMobileNumber(parseInput(employeeDetailsArrFromFile[10]));
						
					} else {
						employeeDetailsMaster.setMobileNumber("000000");
					}
					
					if ((null != employeeDetailsArrFromFile[11]) && ("" != employeeDetailsArrFromFile[11])) {
						employeeDetailsMaster.setDateOfJoining(formatter.parse(parseDate(employeeDetailsArrFromFile[11], "-")));
						
					} else {
						employeeDetailsMaster.setDateOfJoining(formatter.parse("01-01-1900"));
					}
					if ((null != employeeDetailsArrFromFile[12]) && ("" != employeeDetailsArrFromFile[12])) {
						employeeDetailsMaster.setDesignationCode(employeeDetailsArrFromFile[12]);
					} else {
						employeeDetailsMaster.setDesignationCode("");
					}
					if ((null != employeeDetailsArrFromFile[13]) && ("" != employeeDetailsArrFromFile[13])) {
						employeeDetailsMaster.setEmailId(employeeDetailsArrFromFile[13]);
						
					} else {
						employeeDetailsMaster.setEmailId("");
					}
					if ((null != employeeDetailsArrFromFile[14]) && ("" != employeeDetailsArrFromFile[14])) {
						employeeDetailsMaster.setBankName(employeeDetailsArrFromFile[14]);
						
					} else {
						employeeDetailsMaster.setBankName("");
					}
					if ((null != employeeDetailsArrFromFile[15]) && ("" != employeeDetailsArrFromFile[15])) {
						employeeDetailsMaster.setIfscCode(employeeDetailsArrFromFile[15]);
						
					} else {
						employeeDetailsMaster.setIfscCode("");
					}
					if ((null != employeeDetailsArrFromFile[16]) && (!employeeDetailsArrFromFile[16].equals("")) && (!parseInput(employeeDetailsArrFromFile[16]).equals(""))) {
						employeeDetailsMaster.setBankAccountNumber(parseInput(employeeDetailsArrFromFile[16]));
						
					} else {
						employeeDetailsMaster.setBankAccountNumber("0000000");
					}
					if ((null != employeeDetailsArrFromFile[17]) && (!employeeDetailsArrFromFile[17].equals("")) && (!parseInput(employeeDetailsArrFromFile[17]).equals(""))) {
						employeeDetailsMaster.setHra(Float.parseFloat(parseInput(employeeDetailsArrFromFile[17])));
						
					} else {
						employeeDetailsMaster.setHra(0);
					}
					if ((null != employeeDetailsArrFromFile[18]) && (!employeeDetailsArrFromFile[18].equals("")) && (!parseInput(employeeDetailsArrFromFile[18]).equals(""))) {
						employeeDetailsMaster.setDa(Float.parseFloat(parseInput(employeeDetailsArrFromFile[18])));
						
					} else {
						employeeDetailsMaster.setDa(0);
					}
					if ((null != employeeDetailsArrFromFile[19]) && (!employeeDetailsArrFromFile[19].equals("")) && (!parseInput(employeeDetailsArrFromFile[19]).equals(""))) {
						employeeDetailsMaster.setBasic(Float.parseFloat(parseInput(employeeDetailsArrFromFile[19])));
						
					} else {
						employeeDetailsMaster.setBasic(0);
					}
					if ((null != employeeDetailsArrFromFile[20]) && (!employeeDetailsArrFromFile[20].equals("")) && (!parseInput(employeeDetailsArrFromFile[20]).equals(""))) {
						employeeDetailsMaster.setSalary(Float.parseFloat(parseInput(employeeDetailsArrFromFile[20])));
					} else {
						employeeDetailsMaster.setSalary(0);
					}
					if ((null != employeeDetailsArrFromFile[21]) && (!employeeDetailsArrFromFile[21].equals("")) && (!parseInput(employeeDetailsArrFromFile[21]).equals(""))) {
						employeeDetailsMaster .setTotalCasualAlloted(Float.parseFloat(parseInput(employeeDetailsArrFromFile[21])));
						
					} else {
						employeeDetailsMaster .setTotalCasualAlloted(0);
					}
					if ((null != employeeDetailsArrFromFile[22]) && (!employeeDetailsArrFromFile[22].equals("")) && (!parseInput(employeeDetailsArrFromFile[22]).equals(""))) {
						employeeDetailsMaster .setCasualLeavesTaken(Float.parseFloat(parseInput(employeeDetailsArrFromFile[22])));
						
					} else {
						employeeDetailsMaster .setCasualLeavesTaken(0);
					}
					if ((null != employeeDetailsArrFromFile[23]) && (!employeeDetailsArrFromFile[23].equals("")) && (!parseInput(employeeDetailsArrFromFile[23]).equals(""))) {
						employeeDetailsMaster .setCasualLeavesRemaining(Float.parseFloat(parseInput(employeeDetailsArrFromFile[23])));
						
					} else {
						employeeDetailsMaster .setCasualLeavesRemaining(0);
					}
					if ((null != employeeDetailsArrFromFile[24]) && (!employeeDetailsArrFromFile[24].equals("")) && (!parseInput(employeeDetailsArrFromFile[24]).equals(""))) {
						employeeDetailsMaster.setCityCompensationAllowence(Float.parseFloat(parseInput(employeeDetailsArrFromFile[24])));
						
					} else {
						employeeDetailsMaster .setCityCompensationAllowence(0);
					}
					
					
					
					
					employeeDetailsMasterArrList.add(employeeDetailsMaster);

				}
				employeeDetailsMasterRepository.save(employeeDetailsMasterArrList);

			} catch (Exception e) {
				logger.error("****Exception in updateOrAddEmployee()  "+e.getMessage());
				throw e;
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

	public String parseDate(String inputDate, String seperator) {
		String[] dateString = inputDate.split("/");
		return dateString[0] + seperator + dateString[1] + seperator + dateString[2];

	}

	public String parseInput(String inputString) {
		String in = inputString.replaceAll("\"", "");
		return in.replaceAll(",", "");
	}

}
