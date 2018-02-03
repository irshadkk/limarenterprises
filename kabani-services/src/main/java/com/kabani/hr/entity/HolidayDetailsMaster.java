package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class HolidayDetailsMaster {
	
	@Id
	@GeneratedValue
	private int id;
	private String typeOfHoliday;
	private String nameOfHoliday;
	private String descOfHoliday;
	private Date dateOfHoliday;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeOfHoliday() {
		return typeOfHoliday;
	}
	public void setTypeOfHoliday(String typeOfHoliday) {
		this.typeOfHoliday = typeOfHoliday;
	}
	public String getNameOfHoliday() {
		return nameOfHoliday;
	}
	public void setNameOfHoliday(String nameOfHoliday) {
		this.nameOfHoliday = nameOfHoliday;
	}
	public String getDescOfHoliday() {
		return descOfHoliday;
	}
	public void setDescOfHoliday(String descOfHoliday) {
		this.descOfHoliday = descOfHoliday;
	}
	public Date getDateOfHoliday() {
		return dateOfHoliday;
	}
	public void setDateOfHoliday(Date dateOfHoliday) {
		this.dateOfHoliday = dateOfHoliday;
	} 
	 
	

}
