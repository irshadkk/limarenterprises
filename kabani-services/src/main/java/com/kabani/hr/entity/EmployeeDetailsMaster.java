package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class EmployeeDetailsMaster {
	
	@Id
	@GeneratedValue
	private int id;
	private String employeeCode;
	private String employeeBioDeviceCode;
	private String employeeName;
	private String branch;
	private String designation;
	private String department;
	private int employeeAge;
	private String employeeSex;
	private Date dateOfBirth;
	private String nameOfGuardian;
	private String designationCode;
	private Date dateOfJoining;
	private String mobileNumber;
	private String emailId;
	private String bankName;
	private String ifscCode;
	private String bankAccountNumber;
	private float hra;
	private float da;
	private float basic;
	private float salary;
	private float totalCasualAlloted;
	private float casualLeavesTaken;
	private float casualLeavesRemaining;
	private float cityCompensationAllowence;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEmployeeAge() {
		return employeeAge;
	}
	public void setEmployeeAge(int employeeAge) {
		this.employeeAge = employeeAge;
	}
	public String getEmployeeSex() {
		return employeeSex;
	}
	public void setEmployeeSex(String employeeSex) {
		this.employeeSex = employeeSex;
	}
	
	 
	
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeBioDeviceCode() {
		return employeeBioDeviceCode;
	}
	public void setEmployeeBioDeviceCode(String employeeBioDeviceCode) {
		this.employeeBioDeviceCode = employeeBioDeviceCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getNameOfGuardian() {
		return nameOfGuardian;
	}
	public void setNameOfGuardian(String nameOfGuardian) {
		this.nameOfGuardian = nameOfGuardian;
	}
	public String getDesignationCode() {
		return designationCode;
	}
	public void setDesignationCode(String designationCode) {
		this.designationCode = designationCode;
	}
	public Date getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	 
	public float getHra() {
		return hra;
	}
	public void setHra(float hra) {
		this.hra = hra;
	}
	public float getDa() {
		return da;
	}
	public void setDa(float da) {
		this.da = da;
	}
	public float getBasic() {
		return basic;
	}
	public void setBasic(float basic) {
		this.basic = basic;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public float getTotalCasualAlloted() {
		return totalCasualAlloted;
	}
	public void setTotalCasualAlloted(float totalCasualAlloted) {
		this.totalCasualAlloted = totalCasualAlloted;
	}
	public float getCasualLeavesTaken() {
		return casualLeavesTaken;
	}
	public void setCasualLeavesTaken(float casualLeavesTaken) {
		this.casualLeavesTaken = casualLeavesTaken;
	}
	public float getCasualLeavesRemaining() {
		return casualLeavesRemaining;
	}
	public void setCasualLeavesRemaining(float casualLeavesRemaining) {
		this.casualLeavesRemaining = casualLeavesRemaining;
	} 
	public float getCityCompensationAllowence() {
		return cityCompensationAllowence;
	}
	public void setCityCompensationAllowence(float cityCompensationAllowence) {
		this.cityCompensationAllowence = cityCompensationAllowence;
	}
	 
	 
	
	

}
