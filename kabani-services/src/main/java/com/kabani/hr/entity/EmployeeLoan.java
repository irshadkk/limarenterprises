package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EmployeeLoan {

	@Id
	@GeneratedValue
	private int id;
	private String employeeCode;
	private String employeeName;
	private float loanAmount;
	private float loanTenure;
	private Date availDate;
	private String status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public float getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(float loanAmount) {
		this.loanAmount = loanAmount;
	}
	public float getLoanTenure() {
		return loanTenure;
	}
	public void setLoanTenure(float loanTenure) {
		this.loanTenure = loanTenure;
	}
	public Date getAvailDate() {
		return availDate;
	}
	public void setAvailDate(Date availDate) {
		this.availDate = availDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	
	
	
}
