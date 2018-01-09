package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity // This tells Hibernate to make a table out of this class
@IdClass(MyKey.class)
public class UserAttendanceDetails {
	@Id
	private Date date;
	@Id
	private String employeeCode;
	 
	private int day;
	private String month;
	private int year; 
	private String name;
	private String employeeName;
	private String company;
	private String department;
	private String inTime;
	private String outTime;
	private String duration;
	private String lateBy;
	private String earlyBy;
	private String status;
	private String punchRecords;
	private String comment;
	private String branch;
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public String getLateBy() {
		return lateBy;
	}
	public void setLateBy(String lateBy) {
		this.lateBy = lateBy;
	}
	public String getEarlyBy() {
		return earlyBy;
	}
	public void setEarlyBy(String earlyBy) {
		this.earlyBy = earlyBy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPunchRecords() {
		return punchRecords;
	}
	public void setPunchRecords(String punchRecords) {
		this.punchRecords = punchRecords;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
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
	
	
	

}

