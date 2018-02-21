package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EmployeeLoanorAdvanceDeduction {

	@Id
	@GeneratedValue
	private int id;
	private String employeeCode;
	private String employeeName;
	private int loanId;
	private String type;
	private float amount;
	private Date availDate;
	private String status;
	private boolean isLast;

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

	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
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

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public EmployeeLoanorAdvanceDeduction() {
		super();
	}

	public EmployeeLoanorAdvanceDeduction(String employeeCode, String employeeName, int loanId, String type,
			float amount, Date availDate, String status, boolean isLast) {
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.loanId = loanId;
		this.type = type;
		this.amount = amount;
		this.availDate = availDate;
		this.status = status;
		this.isLast = isLast;
	}

}
