package com.kabani.hr.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kabani.hr.entity.EmployeeDetailsMaster;
import com.kabani.hr.entity.SalaryIncometaxSlab;
import com.kabani.hr.entity.SalaryProfessionaltaxSlab;
import com.kabani.hr.entity.SalaryStatus;
import com.kabani.hr.entity.Wps;
import com.kabani.hr.repository.EmployeeDetailsMasterRepository;
import com.kabani.hr.repository.HolidayDetailsMasterRepository;
import com.kabani.hr.repository.SalaryIncometaxSlabRepository;
import com.kabani.hr.repository.SalaryProfessionaltaxSlabRepository;
import com.kabani.hr.repository.SalaryStatusRepository;
import com.kabani.hr.repository.UserAttendanceDetailsRepository;
import com.kabani.hr.repository.WpsRepository;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Component
public class SalaryCalculator {

	@Autowired
	private EmployeeDetailsMasterRepository employeeDetailsMasterRepository;

	@Autowired
	private WpsRepository wpsRepository;

	@Autowired
	private UserAttendanceDetailsRepository userAttendanceDetailsRepository;

	@Autowired
	private SalaryIncometaxSlabRepository salaryIncometaxSlabRepository;
	@Autowired
	private SalaryProfessionaltaxSlabRepository salaryProfessionaltaxSlabRepository;
	@Autowired
	private DaysInYearAndMonthCalculator daysInYearAndMonthCalculator;

	@Autowired
	private HolidayDetailsMasterRepository holidayDetailsMasterRepository;

	@Autowired
	private SalaryStatusRepository salaryStatusRepository;

	public List<Wps> getSalary(int year, int month) {
		return wpsRepository.findForCurrentMonth(year, month);
	}

	public List calculateSalaryOfEmployees(String year, String month) {
		String[] monthArry = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		int indexOfMonth = Arrays.asList(monthArry).indexOf(month);
		int yr = Integer.parseInt(year);
		System.out.println("----------------calculateSalaryOfEmployees-------START-------------------");
		int numberOfWorkingDays = 26;

		int numberOfDaysInMonthYear = daysInYearAndMonthCalculator.calculateDaysInMonthAndYear(yr, indexOfMonth + 1);
		int numberOfHolidaysInMonthYear = holidayDetailsMasterRepository.findCountOfHolidayByYearMonth();
		int numberOfActualWorkingDays = numberOfDaysInMonthYear - numberOfHolidaysInMonthYear;
		int numberOfDaysToAddWithPresentDaysOfEmployeeFor26 = (numberOfActualWorkingDays) != 26
				? (26 - numberOfActualWorkingDays)
				: 0;

		System.out.println("numberOfWorkingDays - " + numberOfWorkingDays);

		ArrayList<Wps> result = new ArrayList();
		// getting all employees details
		List<EmployeeDetailsMaster> allEmployeeMasterDetailsLst = employeeDetailsMasterRepository.findAll();

		// getting all employees full present for this month and year
		List<Object[]> userAttDetailsListFullPresent = userAttendanceDetailsRepository
				.findDistinctPresentInMonth(month);
		// getting all employees half present for this month and year
		List<Object[]> userAttDetailsListHalfPresent = userAttendanceDetailsRepository
				.findDistinctHalfPresentInMonth(month);
		// getting salary incometax slab
		List<SalaryIncometaxSlab> SalaryIncometaxSlabArr = salaryIncometaxSlabRepository.findAll();
		// getting salary professionaltax slab
		List<SalaryProfessionaltaxSlab> SalaryProfessionaltaxSlabArr = salaryProfessionaltaxSlabRepository.findAll();

		if ((null != userAttDetailsListFullPresent && userAttDetailsListFullPresent.size() > 0)
				|| (null != userAttDetailsListHalfPresent && userAttDetailsListHalfPresent.size() > 0)) {
			for (EmployeeDetailsMaster employeeDetailsMasterObj : allEmployeeMasterDetailsLst) {
				Wps wpsOfOneEmployee = new Wps();
				copyDataFromMaster(wpsOfOneEmployee, employeeDetailsMasterObj);
				float halfPresentDays = 0f;
				float fullPresentDays = 0f;
				float totalPresentDays = 0f;
				float totalPresentDaysPlus26PercentageAddition = 0f;

				float totalLeaveDays = 0f;
				float casualLeavesRemaining = 0f;

				float totalSalaryOffered = 0f;
				float taxableIncomePerMonth = 0f;
				float totalIncomeTax = 0f;
				float totalProffessionalTax = 0f;
				float employeeWelfareAmount = 20f;

				float totalPF = 0f;
				float totalESI = 0f;

				wpsOfOneEmployee.setEmployeeCode(employeeDetailsMasterObj.getEmployeeCode());
				wpsOfOneEmployee.setEmployeeName(employeeDetailsMasterObj.getEmployeeName());

				// calculating total half present days
				halfPresentDays = getHalfOrFullPresentDays(userAttDetailsListHalfPresent, employeeDetailsMasterObj);
				wpsOfOneEmployee.setHalfPresentDays(halfPresentDays);

				fullPresentDays = getHalfOrFullPresentDays(userAttDetailsListFullPresent, employeeDetailsMasterObj);
				wpsOfOneEmployee.setFullPresentDays(fullPresentDays);

				// calculating total present days
				totalPresentDays = fullPresentDays + (halfPresentDays / 2);
				wpsOfOneEmployee.setTotalPresentDays(totalPresentDays);

				// adding % of total days adding to all employees to make it 26
				float percentageOfNumberOfDaysToAddWithPresentDaysOfEmployeeFor26 = numberOfDaysToAddWithPresentDaysOfEmployeeFor26
						* (totalPresentDays / numberOfActualWorkingDays);
				totalPresentDays = totalPresentDays + percentageOfNumberOfDaysToAddWithPresentDaysOfEmployeeFor26;
				wpsOfOneEmployee.setTotalPresentDaysPlus26PercentageAddition(totalPresentDays);
				// calculating leaves available for that employee
				if (totalPresentDays < numberOfWorkingDays) {
					totalLeaveDays = numberOfWorkingDays - totalPresentDays;
					casualLeavesRemaining = employeeDetailsMasterObj.getCasualLeavesRemaining();
					// if employee has casual leaves more than the number of leaves he taken,then
					// make totalPresentDays=numberOfWorkingDays; and reduce from leave available
					// and save
					if (casualLeavesRemaining >= totalLeaveDays) {
						totalPresentDays = numberOfWorkingDays;
						employeeDetailsMasterObj
								.setCasualLeavesTaken(employeeDetailsMasterObj.getCasualLeavesTaken() + totalLeaveDays);
						employeeDetailsMasterObj.setCasualLeavesRemaining(casualLeavesRemaining - totalLeaveDays);

					} else if (casualLeavesRemaining < totalLeaveDays) {
						totalPresentDays = totalPresentDays + casualLeavesRemaining;
						employeeDetailsMasterObj.setCasualLeavesTaken(
								employeeDetailsMasterObj.getCasualLeavesTaken() + casualLeavesRemaining);
						employeeDetailsMasterObj.setCasualLeavesRemaining(0);
					}
					employeeDetailsMasterRepository.save(employeeDetailsMasterObj);

				}
				// setting totalPresentDays after all additions
				wpsOfOneEmployee.setDaysOfAttandance(totalPresentDays);
				wpsOfOneEmployee.setLossOfPayDays(numberOfWorkingDays - totalPresentDays);

				// finding salary offered by the company
				totalSalaryOffered = employeeDetailsMasterObj.getSalary();
				wpsOfOneEmployee.setGrossMonthlyWages(totalSalaryOffered);
				wpsOfOneEmployee.setTotalSalaryOffered(totalSalaryOffered);

				// finding income tax
				totalIncomeTax = getIncomeTaxOfAnEmployee(SalaryIncometaxSlabArr, totalSalaryOffered);
				wpsOfOneEmployee.setTotalIncomeTax(totalIncomeTax);
				wpsOfOneEmployee.setTaxDeductedAtSource(totalIncomeTax);

				// finding professional tax
				totalProffessionalTax = getProfessionalTaxOfAnEmployee(SalaryProfessionaltaxSlabArr,
						totalSalaryOffered);
				wpsOfOneEmployee.setTotalProfessionalTax(totalProffessionalTax);

				// employee welfare fund
				wpsOfOneEmployee.setTotalEmployeeWelfareFund(getWelfareFundOfAnEmployee(employeeDetailsMasterObj,
						totalSalaryOffered, employeeWelfareAmount));

				// pf only for those who have salary less than 15000 12 % of offered salary
				totalPF = getPFOfanEmployee(totalSalaryOffered);
				wpsOfOneEmployee.setTotalPF(totalPF);

				// esi is applicable for all and reducing 1% for this year
				totalESI = getESIOfanEmployee(totalSalaryOffered);
				wpsOfOneEmployee.setTotalESI(totalESI);

				// calculating salary
				float salary = calculateSalary(wpsOfOneEmployee, totalSalaryOffered, numberOfWorkingDays);
				wpsOfOneEmployee.setNetWagesPaid(salary);
				wpsOfOneEmployee.setTotalSalaryForThisMonth(salary);

				wpsOfOneEmployee
						.setDateOfPayment(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				wpsOfOneEmployee.setYear(Integer.parseInt(year));
				wpsOfOneEmployee.setMonth(indexOfMonth + 1);
				result.add(wpsOfOneEmployee);

			}

		}

		wpsRepository.save(result);
		salaryStatusRepository.save(new SalaryStatus(indexOfMonth + 1, Integer.parseInt(year),
				Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())));
		System.out.println("----------------calculateSalaryOfEmployees-------END-------------------");
		return result;
	}

	private void copyDataFromMaster(Wps wps, EmployeeDetailsMaster master) {
		wps.setBankAccountNumber(master.getBankAccountNumber());
		wps.setBankName(master.getBankName());
		wps.setBasic(master.getBasic());
		wps.setBranch(master.getBranch());
		wps.setCasualLeavesRemaining(master.getCasualLeavesRemaining());
		wps.setCasualLeavesTaken(master.getCasualLeavesTaken());
		wps.setCityCompensationAllowence(master.getCasualLeavesTaken());
		wps.setDa(master.getDa());
		wps.setDateOfBirth(master.getDateOfBirth());
		wps.setDateOfJoining(master.getDateOfJoining());
		wps.setDepartment(master.getDepartment());
		wps.setDesignation(master.getDesignation());
		wps.setDesignationCode(master.getDesignationCode());
		wps.setEmailId(master.getEmailId());
		wps.setEmployeeAge(master.getEmployeeAge());
		wps.setEmployeeCode(master.getEmployeeCode());
		wps.setEmployeeName(master.getEmployeeName());
		wps.setEmployeeSex(master.getEmployeeSex());
		wps.setHra(master.getHra());
		wps.setIfscCode(master.getIfscCode());
		wps.setMobileNumber(master.getMobileNumber());
		wps.setNameOfGuardian(master.getNameOfGuardian());
		wps.setTotalSalaryOffered(master.getSalary());
		wps.setTotalCasualAlloted(master.getTotalCasualAlloted());

		wps.setNumberOfWeeklyOffGranted(master.getNumberOfWeeklyOffGranted());
		wps.setNumberOfLeaveGranted(master.getNumberOfLeaveGranted());
		wps.setTotalCasualAlloted(master.getTotalCasualAlloted());
		wps.setOverTimeWages(master.getOverTimeWages());
		wps.setLeaveWages(master.getLeaveWages());
		wps.setNationalAndFestivalHolidayWages(master.getNationalAndFestivalHolidayWages());
		wps.setArrearPaid(master.getArrearPaid());
		wps.setBonus(master.getBonus());
		wps.setMaternityBenefit(master.getMaternityBenefit());
		wps.setOtherAllowances(master.getOtherAllowances());
		wps.setTotalStaffAdvance(master.getTotalStaffAdvance());
		wps.setTotalSalaryAdvance(master.getTotalSalaryAdvance());
		wps.setAdvanceTotalAmount(master.getAdvanceTotalAmount());
		wps.setDeductionOfFine(master.getDeductionOfFine());
		wps.setDeductionForLossAndDamages(master.getDeductionForLossAndDamages());
		wps.setTotalLineShort(master.getTotalLineShort());
		wps.setOtherDeduction(master.getOtherDeduction());
		wps.setTotalDeduction(master.getTotalDeduction());

	}

	/**
	 * @param totalSalaryOffered
	 * @return
	 */
	private float getESIOfanEmployee(float totalSalaryOffered) {
		return (totalSalaryOffered * 1 / 100);
	}

	/**
	 * @param wpsOfOneEmployee
	 * @param totalSalaryOffered
	 */
	private float getPFOfanEmployee(float totalSalaryOffered) {
		float totalPF = 0f;
		if (totalSalaryOffered < 15000) {
			totalPF = (totalSalaryOffered * 12 / 100);
		} else {
			totalPF = 0;
		}
		return totalPF;
	}

	/**
	 * @param employeeDetailsMasterObj
	 * @param wpsOfOneEmployee
	 * @param totalSalaryOffered
	 * @param employeeWelfareAmount
	 */
	private float getWelfareFundOfAnEmployee(EmployeeDetailsMaster employeeDetailsMasterObj, float totalSalaryOffered,
			float employeeWelfareAmount) {
		float employeeWelfare = 0f;
		if (employeeDetailsMasterObj.getEmployeeAge() < 55 && totalSalaryOffered >= 15000) {
			employeeWelfare = employeeWelfareAmount;
		} else {
			employeeWelfare = 0;
		}
		return employeeWelfare;
	}

	/**
	 * @param SalaryProfessionaltaxSlabArr
	 */
	private float getProfessionalTaxOfAnEmployee(List<SalaryProfessionaltaxSlab> SalaryProfessionaltaxSlabArr,
			float totalSalaryOffered) {
		float totalProffessionalTax = 0f;
		for (SalaryProfessionaltaxSlab objSalaryProfessionaltaxSlab : SalaryProfessionaltaxSlabArr) {

			if ((totalSalaryOffered) >= objSalaryProfessionaltaxSlab.getSlabamountstart()
					&& (totalSalaryOffered) <= objSalaryProfessionaltaxSlab.getSlabamountend()) {
				totalProffessionalTax = objSalaryProfessionaltaxSlab.getSlabamount();
				break;
			}
		}
		return totalProffessionalTax;
	}

	/**
	 * @param SalaryIncometaxSlabArr
	 * @param wpsOfOneEmployee
	 * @param totalSalaryOffered
	 */
	private float getIncomeTaxOfAnEmployee(List<SalaryIncometaxSlab> SalaryIncometaxSlabArr, float totalSalaryOffered) {
		float taxableIncomePerMonth;
		float totalIncomeTax = 0f;
		// finding income tax
		for (SalaryIncometaxSlab objSalaryIncometaxSlab : SalaryIncometaxSlabArr) {

			if ((totalSalaryOffered * 12) >= objSalaryIncometaxSlab.getSlabamountstart()
					&& (totalSalaryOffered * 12) <= objSalaryIncometaxSlab.getSlabamountend()) {
				taxableIncomePerMonth = ((totalSalaryOffered * 12) - (objSalaryIncometaxSlab.getSlabamountstart()))
						/ 12;
				totalIncomeTax = ((taxableIncomePerMonth * objSalaryIncometaxSlab.getSlabpercentage()) / 100)
						+ (objSalaryIncometaxSlab.getSlabadditionalamount() / 12);

				break;
			}
		}
		return totalIncomeTax;
	}

	/**
	 * @param userAttDetailsListHalfPresent
	 * @param employeeDetailsMasterObj
	 * @param halfPresentDays
	 * @return
	 */
	private float getHalfOrFullPresentDays(List<Object[]> userAttDetailsListHalfPresent,
			EmployeeDetailsMaster employeeDetailsMasterObj) {
		float halfPresentDays = 0f;
		for (Object[] halfPresentObj : userAttDetailsListHalfPresent) {

			if (("" + halfPresentObj[1]).equals("" + employeeDetailsMasterObj.getEmployeeBioDeviceCode())) {
				halfPresentDays = Float.parseFloat("" + halfPresentObj[0]);
				break;
			}

		}
		return halfPresentDays;
	}

	public float calculateSalary(Wps wpsObj, float totalSalaryOffered, float numberOfWorkingDays) {
		float salary = 0f;
		float totalPresentDays = wpsObj.getDaysOfAttandance();
		float totalIncomeTax = wpsObj.getTotalIncomeTax();
		float totalProfessionalTax = wpsObj.getTotalProfessionalTax();
		float totalPF = wpsObj.getTotalPF();
		float totalESI = wpsObj.getTotalESI();
		float totalStaffAdvance = wpsObj.getTotalStaffAdvance();
		float totalLineShort = wpsObj.getTotalLineShort();
		float totalSalaryAdvance = wpsObj.getTotalSalaryAdvance();
		float employeeWelfareAmount = wpsObj.getTotalEmployeeWelfareFund();

		float totalEarnedThisMonth = totalSalaryOffered * totalPresentDays / numberOfWorkingDays;
		float totalDeductedThisMonth = totalIncomeTax + totalProfessionalTax + totalPF + totalESI + totalStaffAdvance
				+ totalLineShort + totalSalaryAdvance + employeeWelfareAmount;
		salary = totalEarnedThisMonth - totalDeductedThisMonth;

		return salary;
	}

	public int hasSalaryGenerated(String year, String month) {
		int returnValue = 0;
		returnValue = salaryStatusRepository.isSalaryGenerated(Integer.parseInt(year), Integer.parseInt(month));
		return returnValue;
	}

	public WritableWorkbook genereteExcel(int year, int month, HttpServletResponse response) {
		String fileName = "salary_wps.xls";
		WritableWorkbook workbook = null;
		String[] heading = { "Employee Code", "Employee Name", "Name of Father/Husband", "Sex", "Date Of Birth",
				"Designation", "Designation Code/ Grade as in Government Order", "Date of Joining", "Mobile Number",
				"Email Id", "Bank Name", "IFSC Code", "Bank Account Number", "Days of Attendance", "Loss of pay days",
				"Number of weekly off granted", "Number of Leave granted", "Basic", "DA", "HRA",
				"City Compensation allowances", "Gross Monthly Wages", "Overtime wages", "Leave wages",
				"National & Festival Holidays wages", "Arrear paid", "Bonus", "Maternity Benefit", "Other Allowances",
				"Advance", "Total Amount", "Employees Provident Fund", "Employees State Insurance", "Advances",
				"Welfare Fund", "Professional Tax", "Tax Deducted at Source", "Deduction of Fine",
				"Deduction  for  Loss & Damages", "Other Deduction", "Total Deduction", "Net wages paid",
				"Date of payment", "Remarks" };

		try {
			List<Wps> returnValue = wpsRepository.findForCurrentMonth(year, month);
			Object[][] salaryExcel = new Object[returnValue.size()][heading.length];
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			
			workbook = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet excelOutputsheet = workbook.createSheet("Salary", 0);
			addExcelOutputHeader(excelOutputsheet,heading);
	         writeExcelOutputData(excelOutputsheet,returnValue);
	           
			workbook.write();
			workbook.close();
			 
			 

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 catch ( Exception e) {
				e.printStackTrace();
			}
		return workbook;

	}
	private void addExcelOutputHeader(WritableSheet sheet,String[] heading) throws RowsExceededException, WriteException{
	        for (int cell=0;cell<heading.length;cell++) {
	        	sheet.addCell(new Label(cell, 0, heading[cell]));
			}   
		 
	    }
	 private void writeExcelOutputData(WritableSheet sheet,List<Wps> returnValue) throws RowsExceededException, WriteException{
         
	       for(int rowNo = 1; rowNo<=returnValue.size(); rowNo++){
	    	   for(int colNo = 0; colNo<=24; colNo++){
	    		   //Wps.getSomething()
	              sheet.addCell(new Label(colNo, rowNo, "Col Data "));
	               
	    	   }

	       }

	    }

}
