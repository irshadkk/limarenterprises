package com.kabani.hr.entity;

import java.util.Objects;

public class UserAttendanceGrid implements Comparable {
	private String employeeId;
	private String employeeName;
	private String[] employeeAttndance = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "" };

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
