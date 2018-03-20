package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EmployeeIncentive {

	@Id
	@GeneratedValue
	private int id;
	private String employeeCode;
	private String employeeName;
	private float amount;
	private Date date;


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

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public EmployeeIncentive() {
		super();
	}

	public EmployeeIncentive(String employeeCode, String employeeName, float amount, Date availDate) {
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.amount = amount;
		this.date = availDate;
	}

}
