package com.kabani.hr.entity;

public class Salary {
 private String employeeCode;
 private String name;
 private float fullPresentDays;
 private float halfPresentDays;
 private float totalPresentDays;
 private String salaryCalculation;
  
 private float salary;

public String getEmployeeCode() {
	return employeeCode;
}

public void setEmployeeCode(String employeeCode) {
	this.employeeCode = employeeCode;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public float getFullPresentDays() {
	return fullPresentDays;
}

public void setFullPresentDays(float fullPresentDays) {
	this.fullPresentDays = fullPresentDays;
}

public float getHalfPresentDays() {
	return halfPresentDays;
}

public void setHalfPresentDays(float halfPresentDays) {
	this.halfPresentDays = halfPresentDays;
}

public float getTotalPresentDays() {
	return totalPresentDays;
}

public void setTotalPresentDays(float totalPresentDays) {
	this.totalPresentDays = totalPresentDays;
}

public String getSalaryCalculation() {
	return salaryCalculation;
}

public void setSalaryCalculation(String salaryCalculation) {
	this.salaryCalculation = salaryCalculation;
}

public float getSalary() {
	return salary;
}

public void setSalary(float salary) {
	this.salary = salary;
}
  
  
}
