package com.kabani.hr.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Wps {

	@Id
	@GeneratedValue
	private int id;
	private String employeeCode;
	private String employeeName;
	private String nameOfGuardian;
	private String employeeSex;
	private Date dateOfBirth;
	private float employeeAge;
	private String designation;
	private String designationCode;
	private String branch;
	private String department;
	private Date dateOfJoining;
	private String mobileNumber;
	private String emailId;
	private String bankName;
	private String ifscCode;
	private String bankAccountNumber;
	private float fullPresentDays;
	private float halfPresentDays;
	private float totalPresentDays;
	private float totalPresentDaysPlus26PercentageAddition;
	private float daysOfAttandance;
	private float lossOfPayDays;
	private float numberOfWeeklyOffGranted;
	private float numberOfLeaveGranted;
	private float hra;
	private float da;
	private float basic;
	private float totalCasualAlloted;
	private float casualLeavesTaken;
	private float casualLeavesRemaining;
	private float cityCompensationAllowence;
	private float grossMonthlyWages;
	private float totalSalaryOffered;
	private float overTimeWages;
	private float leaveWages;
	private float nationalAndFestivalHolidayWages;
	private float arrearPaid;
	private float bonus;
	private float maternityBenefit;
	private float otherAllowances;
	private float totalStaffAdvance;
	private float totalSalaryAdvance;
	private float advanceTotalAmount;
	private float totalPF;
	private float totalESI;
	private float totalEmployeeWelfareFund;
	private float totalIncomeTax;// Income Tax
	private float taxDeductedAtSource;// Income Tax
	private float totalProfessionalTax;// Income Tax
	private float deductionOfFine;
	private float deductionForLossAndDamages;
	private float totalLineShort;
	private float otherDeduction;
	private float totalDeduction;
	private float netWagesPaid;
	private float totalSalaryForThisMonth;

	private int month;
	private int year;

	public float getTotalSalaryOffered() {
		return totalSalaryOffered;
	}

	public void setTotalSalaryOffered(float totalSalaryOffered) {
		this.totalSalaryOffered = totalSalaryOffered;
	}

	private Date DateOfPayment;

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

	public String getNameOfGuardian() {
		return nameOfGuardian;
	}

	public void setNameOfGuardian(String nameOfGuardian) {
		this.nameOfGuardian = nameOfGuardian;
	}

	public String getEmployeeSex() {
		return employeeSex;
	}

	public void setEmployeeSex(String employeeSex) {
		this.employeeSex = employeeSex;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public float getEmployeeAge() {
		return employeeAge;
	}

	public void setEmployeeAge(float employeeAge) {
		this.employeeAge = employeeAge;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDesignationCode() {
		return designationCode;
	}

	public void setDesignationCode(String designationCode) {
		this.designationCode = designationCode;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
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

	public float getDaysOfAttandance() {
		return daysOfAttandance;
	}

	public void setDaysOfAttandance(float daysOfAttandance) {
		this.daysOfAttandance = daysOfAttandance;
	}

	public float getLossOfPayDays() {
		return lossOfPayDays;
	}

	public void setLossOfPayDays(float lossOfPayDays) {
		this.lossOfPayDays = lossOfPayDays;
	}

	public float getNumberOfWeeklyOffGranted() {
		return numberOfWeeklyOffGranted;
	}

	public void setNumberOfWeeklyOffGranted(float numberOfWeeklyOffGranted) {
		this.numberOfWeeklyOffGranted = numberOfWeeklyOffGranted;
	}

	public float getNumberOfLeaveGranted() {
		return numberOfLeaveGranted;
	}

	public void setNumberOfLeaveGranted(float numberOfLeaveGranted) {
		this.numberOfLeaveGranted = numberOfLeaveGranted;
	}

	public float getHra() {
		return hra;
	}

	public void setHra(float hra) {
		this.hra = hra;
	}

	public float getDa() {
		return da;
	}

	public void setDa(float da) {
		this.da = da;
	}

	public float getBasic() {
		return basic;
	}

	public void setBasic(float basic) {
		this.basic = basic;
	}

	public float getTotalCasualAlloted() {
		return totalCasualAlloted;
	}

	public void setTotalCasualAlloted(float totalCasualAlloted) {
		this.totalCasualAlloted = totalCasualAlloted;
	}

	public float getCasualLeavesTaken() {
		return casualLeavesTaken;
	}

	public void setCasualLeavesTaken(float casualLeavesTaken) {
		this.casualLeavesTaken = casualLeavesTaken;
	}

	public float getCasualLeavesRemaining() {
		return casualLeavesRemaining;
	}

	public void setCasualLeavesRemaining(float casualLeavesRemaining) {
		this.casualLeavesRemaining = casualLeavesRemaining;
	}

	public float getCityCompensationAllowence() {
		return cityCompensationAllowence;
	}

	public void setCityCompensationAllowence(float cityCompensationAllowence) {
		this.cityCompensationAllowence = cityCompensationAllowence;
	}

	public float getGrossMonthlyWages() {
		return grossMonthlyWages;
	}

	public void setGrossMonthlyWages(float grossMonthlyWages) {
		this.grossMonthlyWages = grossMonthlyWages;
	}

	public float getOverTimeWages() {
		return overTimeWages;
	}

	public void setOverTimeWages(float overTimeWages) {
		this.overTimeWages = overTimeWages;
	}

	public float getLeaveWages() {
		return leaveWages;
	}

	public void setLeaveWages(float leaveWages) {
		this.leaveWages = leaveWages;
	}

	public float getNationalAndFestivalHolidayWages() {
		return nationalAndFestivalHolidayWages;
	}

	public void setNationalAndFestivalHolidayWages(float nationalAndFestivalHolidayWages) {
		this.nationalAndFestivalHolidayWages = nationalAndFestivalHolidayWages;
	}

	public float getArrearPaid() {
		return arrearPaid;
	}

	public void setArrearPaid(float arrearPaid) {
		this.arrearPaid = arrearPaid;
	}

	public float getBonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}

	public float getMaternityBenefit() {
		return maternityBenefit;
	}

	public void setMaternityBenefit(float maternityBenefit) {
		this.maternityBenefit = maternityBenefit;
	}

	public float getOtherAllowances() {
		return otherAllowances;
	}

	public void setOtherAllowances(float otherAllowances) {
		this.otherAllowances = otherAllowances;
	}

	public float getTotalStaffAdvance() {
		return totalStaffAdvance;
	}

	public void setTotalStaffAdvance(float totalStaffAdvance) {
		this.totalStaffAdvance = totalStaffAdvance;
	}

	public float getTotalSalaryAdvance() {
		return totalSalaryAdvance;
	}

	public void setTotalSalaryAdvance(float totalSalaryAdvance) {
		this.totalSalaryAdvance = totalSalaryAdvance;
	}

	public float getAdvanceTotalAmount() {
		return advanceTotalAmount;
	}

	public void setAdvanceTotalAmount(float advanceTotalAmount) {
		this.advanceTotalAmount = advanceTotalAmount;
	}

	public float getTotalPF() {
		return totalPF;
	}

	public void setTotalPF(float totalPF) {
		this.totalPF = totalPF;
	}

	public float getTotalESI() {
		return totalESI;
	}

	public void setTotalESI(float totalESI) {
		this.totalESI = totalESI;
	}

	public float getTotalEmployeeWelfareFund() {
		return totalEmployeeWelfareFund;
	}

	public void setTotalEmployeeWelfareFund(float totalEmployeeWelfareFund) {
		this.totalEmployeeWelfareFund = totalEmployeeWelfareFund;
	}

	public float getTaxDeductedAtSource() {
		return taxDeductedAtSource;
	}

	public void setTaxDeductedAtSource(float taxDeductedAtSource) {
		this.taxDeductedAtSource = taxDeductedAtSource;
	}

	public float getDeductionOfFine() {
		return deductionOfFine;
	}

	public void setDeductionOfFine(float deductionOfFine) {
		this.deductionOfFine = deductionOfFine;
	}

	public float getDeductionForLossAndDamages() {
		return deductionForLossAndDamages;
	}

	public void setDeductionForLossAndDamages(float deductionForLossAndDamages) {
		this.deductionForLossAndDamages = deductionForLossAndDamages;
	}

	public float getTotalLineShort() {
		return totalLineShort;
	}

	public void setTotalLineShort(float totalLineShort) {
		this.totalLineShort = totalLineShort;
	}

	public float getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(float otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public float getTotalDeduction() {
		return totalDeduction;
	}

	public void setTotalDeduction(float totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	public float getNetWagesPaid() {
		return netWagesPaid;
	}

	public void setNetWagesPaid(float netWagesPaid) {
		this.netWagesPaid = netWagesPaid;
	}

	public Date getDateOfPayment() {
		return DateOfPayment;
	}

	public void setDateOfPayment(Date dateOfPayment) {
		DateOfPayment = dateOfPayment;
	}

	public float getTotalPresentDaysPlus26PercentageAddition() {
		return totalPresentDaysPlus26PercentageAddition;
	}

	public void setTotalPresentDaysPlus26PercentageAddition(float totalPresentDaysPlus26PercentageAddition) {
		this.totalPresentDaysPlus26PercentageAddition = totalPresentDaysPlus26PercentageAddition;
	}

	public float getTotalIncomeTax() {
		return totalIncomeTax;
	}

	public void setTotalIncomeTax(float totalIncomeTax) {
		this.totalIncomeTax = totalIncomeTax;
	}

	public float getTotalProfessionalTax() {
		return totalProfessionalTax;
	}

	public void setTotalProfessionalTax(float totalProfessionalTax) {
		this.totalProfessionalTax = totalProfessionalTax;
	}

	public float getTotalSalaryForThisMonth() {
		return totalSalaryForThisMonth;
	}

	public void setTotalSalaryForThisMonth(float totalSalaryForThisMonth) {
		this.totalSalaryForThisMonth = totalSalaryForThisMonth;
	}

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
}
