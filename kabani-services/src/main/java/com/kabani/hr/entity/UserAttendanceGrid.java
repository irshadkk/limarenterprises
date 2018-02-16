package com.kabani.hr.entity;

import java.util.Objects;

public class UserAttendanceGrid implements Comparable {
	private String employeeId;
	private String employeeName;
	private String[] employeeAttndance = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "" };
	
	private String month;
	private int year; 
	private String status;
	private String branch;
	private String company;
	private String department;
	private String inTime;
	private String outTime;
	private String duration;

	public UserAttendanceGrid(String employeeId, String employeeName) {
		this.employeeId = employeeId;
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String[] getEmployeeAttndance() {
		return employeeAttndance;
	}

	public void setEmployeeAttndance(String[] employeeAttndance) {
		this.employeeAttndance = employeeAttndance;
	}
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserAttendanceGrid other = (UserAttendanceGrid) obj;

		return this.employeeId.equals(other.employeeId) && this.employeeName.equals(other.employeeName);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.employeeId.hashCode() * this.employeeName.hashCode());
	}

	@Override
	public String toString() {
		return this.employeeId + " " + this.employeeName;
	}

	
	public int compareTo(Object temp) {
		return Integer.parseInt(this.employeeId)-(Integer.parseInt(((UserAttendanceGrid)temp).getEmployeeId()));
	}
}
