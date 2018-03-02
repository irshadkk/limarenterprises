package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity // This tells Hibernate to make a table out of this class
@IdClass(SalaryStatusKey.class)
public class SalaryStatus {
	@Id
	private int month;
	@Id
	private int year;
	@Id
	private String type;

	private Date generatedOn;

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getGeneratedOn() {
		return generatedOn;
	}

	public void setGeneratedOn(Date generatedOn) {
		this.generatedOn = generatedOn;
	}

	public SalaryStatus() {

	}

	public SalaryStatus(int month, int year, Date generatedOn, String type) {
		this.month = month;
		this.year = year;
		this.generatedOn = generatedOn;
		this.type = type;
	}

}