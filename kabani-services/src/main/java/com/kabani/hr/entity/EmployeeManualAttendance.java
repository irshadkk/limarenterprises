package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class EmployeeManualAttendance {

	@Id
	@GeneratedValue
	private int id;
	private String employeeCode; 
	private String employeeName;  
	private float twentySixPercentageAddition;
	private float extraDaysWorked;
	private Date dayOfextraDay;
	private float otherExtraDays; 
	private Date dayOfOtherExtraDay;
	public Date getDayOfextraDay() {
		return dayOfextraDay;
	}
	public void setDayOfextraDay(Date dayOfextraDay) {
		this.dayOfextraDay = dayOfextraDay;
	}
	public Date getDayOfOtherExtraDay() {
		return dayOfOtherExtraDay;
	}
	public void setDayOfOtherExtraDay(Date dayOfOtherExtraDay) {
		this.dayOfOtherExtraDay = dayOfOtherExtraDay;
	}
	private Date dayOfAddition;
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
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	 
	public float getTwentySixPercentageAddition() {
		return twentySixPercentageAddition;
	}
	public void setTwentySixPercentageAddition(float twentySixPercentageAddition) {
		this.twentySixPercentageAddition = twentySixPercentageAddition;
	}
	public float getExtraDaysWorked() {
		return extraDaysWorked;
	}
	public void setExtraDaysWorked(float extraDaysWorked) {
		this.extraDaysWorked = extraDaysWorked;
	}
	public float getOtherExtraDays() {
		return otherExtraDays;
	}
	public void setOtherExtraDays(float otherExtraDays) {
		this.otherExtraDays = otherExtraDays;
	}
	public Date getDayOfAddition() {
		return dayOfAddition;
	}
	public void setDayOfAddition(Date dayOfAddition) {
		this.dayOfAddition = dayOfAddition;
	}
	
	

	  
}
