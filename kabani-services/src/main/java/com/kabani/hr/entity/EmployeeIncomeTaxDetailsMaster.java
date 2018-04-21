package com.kabani.hr.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(IncomeTaxMasterKey.class)
public class EmployeeIncomeTaxDetailsMaster {
    @Id
	private String employeeCode;
	@Id
	private int taxForTheYear; 
	@Id
	private int taxForTheMonth;
	
	
	
	private String employeeName;
	private float monthlySalry;
	private float taxAmount; 
	private String status;
	 
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public float getMonthlySalry() {
		return monthlySalry;
	}
	public void setMonthlySalry(float monthlySalry) {
		this.monthlySalry = monthlySalry;
	}
	public float getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(float taxAmount) {
		this.taxAmount = taxAmount;
	}
 
	public int getTaxForTheYear() {
		return taxForTheYear;
	}
	public void setTaxForTheYear(int taxForTheYear) {
		this.taxForTheYear = taxForTheYear;
	}
	public int getTaxForTheMonth() {
		return taxForTheMonth;
	}
	public void setTaxForTheMonth(int taxForTheMonth) {
		this.taxForTheMonth = taxForTheMonth;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
	
	
	
	
}
