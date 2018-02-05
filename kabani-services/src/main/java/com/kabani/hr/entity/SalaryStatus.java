package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class SalaryStatus {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
    private int month;

    private int year;
    
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getGeneratedOn() {
		return generatedOn;
	}

	public void setGeneratedOn(Date generatedOn) {
		this.generatedOn = generatedOn;
	}

	public SalaryStatus(int month,int year, Date generatedOn){
		this.month=month;
		this.year=year;
		this.generatedOn = generatedOn;
	}
	
}