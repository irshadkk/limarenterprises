package com.kabani.hr.helper;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Component
public class SalaryCalculator {
	private final Logger logger = LogManager.getLogger(this.getClass());
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

	public List calculateSalaryOfEmployees(String year, String month) throws Exception {
		String[] monthArry = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		int indexOfMonth = Arrays.asList(monthArry).indexOf(month);
		int yr = Integer.parseInt(year);
		logger.info("----------------calculateSalaryOfEmployees" + year + "--" + month + "-----START-------------------");
		int numberOfWorkingDays = 26;

		ArrayList<Wps> result;
		try {
			int numberOfDaysInMonthYear = daysInYearAndMonthCalculator.calculateDaysInMonthAndYear(yr,indexOfMonth + 1);
			int numberOfHolidaysInMonthYear = holidayDetailsMasterRepository.findCountOfHolidayByYearMonth(yr,indexOfMonth+ 1);
			int numberOfActualWorkingDays = numberOfDaysInMonthYear - numberOfHolidaysInMonthYear;
			int numberOfDaysToAddWithPresentDaysOfEmployeeFor26 = (numberOfActualWorkingDays != 26) ? (26 - numberOfActualWorkingDays): 0;

			result = new ArrayList();
			// getting all employees details
			List<EmployeeDetailsMaster> allEmployeeMasterDetailsLst = employeeDetailsMasterRepository.findAll();

			// getting all employees full present for this month and year
			List<Object[]> userAttDetailsListFullPresent = userAttendanceDetailsRepository.findDistinctPresentInMonthYear(indexOfMonth+ 1,yr);
			// getting all employees half present for this month and year
			List<Object[]> userAttDetailsListHalfPresent = userAttendanceDetailsRepository.findDistinctHalfPresentInMonthYear(indexOfMonth+ 1,yr);
			// getting all employees casual leaves for this month and year
			List<Object[]> userAttDetailsListCasualLeave = userAttendanceDetailsRepository.findDistinctCasualLeaveInMonthYear(indexOfMonth+ 1,yr);
			// getting salary incometax slab
			List<SalaryIncometaxSlab> SalaryIncometaxSlabArr = salaryIncometaxSlabRepository.findAll();
			// getting salary professionaltax slab
			List<SalaryProfessionaltaxSlab> SalaryProfessionaltaxSlabArr = salaryProfessionaltaxSlabRepository.findAll();

			if ((null != userAttDetailsListFullPresent && userAttDetailsListFullPresent.size() > 0)
					|| (null != userAttDetailsListHalfPresent && userAttDetailsListHalfPresent.size() > 0)
					|| (null != userAttDetailsListCasualLeave && userAttDetailsListCasualLeave.size() > 0)
					) {
				for (EmployeeDetailsMaster employeeDetailsMasterObj : allEmployeeMasterDetailsLst) {
					float halfPresentDays = 0f;
					float fullPresentDays = 0f;
					float casualLeavesTaken = 0f;
					float totalPresentDays = 0f;
					// calculating total half present days
					halfPresentDays = getHalfOrFullPresentDays(userAttDetailsListHalfPresent, employeeDetailsMasterObj);
					fullPresentDays = getHalfOrFullPresentDays(userAttDetailsListFullPresent, employeeDetailsMasterObj);
					casualLeavesTaken = getCasualLeaveDays(userAttDetailsListCasualLeave, employeeDetailsMasterObj);
					// adding % of total days adding to all employees to make it 26
					
					// calculating total present days 
					float percentageOfNumberOfDaysToAddWithPresentDaysOfEmployeeFor26 = numberOfDaysToAddWithPresentDaysOfEmployeeFor26
							* ((( fullPresentDays) + (halfPresentDays / 2)+(casualLeavesTaken)) / numberOfActualWorkingDays);
					totalPresentDays = fullPresentDays + (halfPresentDays / 2)+casualLeavesTaken+ percentageOfNumberOfDaysToAddWithPresentDaysOfEmployeeFor26;
					
					
                    if(totalPresentDays>0) {// only generating salary if employee is present atleast once in a month
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
                    	Wps wpsOfOneEmployee = new Wps();
    					copyDataFromMaster(wpsOfOneEmployee, employeeDetailsMasterObj);
    					wpsOfOneEmployee.setEmployeeCode(employeeDetailsMasterObj.getEmployeeCode());
    					wpsOfOneEmployee.setEmployeeName(employeeDetailsMasterObj.getEmployeeName());
    					wpsOfOneEmployee.setHalfPresentDays(halfPresentDays);
    					wpsOfOneEmployee.setFullPresentDays(fullPresentDays);
    					wpsOfOneEmployee.setTotalCasualLeavesDays(casualLeavesTaken);
    					employeeDetailsMasterObj.setCasualLeavesRemaining(employeeDetailsMasterObj.getCasualLeavesRemaining()-casualLeavesTaken);
    					employeeDetailsMasterRepository.save(employeeDetailsMasterObj);
    					
    					wpsOfOneEmployee.setTotalPresentDaysPlus26PercentageAddition(totalPresentDays);
    					 
    					wpsOfOneEmployee.setTotalPresentDays(totalPresentDays);
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

    					wpsOfOneEmployee.setDateOfPayment(
    							Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    					wpsOfOneEmployee.setYear(Integer.parseInt(year));
    					wpsOfOneEmployee.setMonth(indexOfMonth + 1);
    					result.add(wpsOfOneEmployee);
                    }
					

				}

			}

			wpsRepository.save(result);
			salaryStatusRepository.save(new SalaryStatus(indexOfMonth + 1, Integer.parseInt(year),
					Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())));
		} catch (NumberFormatException e) {
			logger.error("****Exception in calculateSalaryOfEmployees() " + e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("****Exception in calculateSalaryOfEmployees() " + e.getMessage());
			throw e;
		}
		logger.info("----------------calculateSalaryOfEmployees" + year + "--" + month + "-----END-------------------");
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
	private float getPFOfanEmployee(float totalSalaryOffered) throws Exception {
		float totalPF = 0f;
		try {
			if (totalSalaryOffered < 15000) {
				totalPF = (totalSalaryOffered * 12 / 100);
			} else {
				totalPF = 0;
			}
		} catch (Exception e) {
			logger.error("****Exception in getPFOfanEmployee() " + e.getMessage());
			throw e;
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
			float employeeWelfareAmount) throws Exception {
		float employeeWelfare = 0f;
		try {
			if (employeeDetailsMasterObj.getEmployeeAge() < 55 && totalSalaryOffered >= 15000) {
				employeeWelfare = employeeWelfareAmount;
			} else {
				employeeWelfare = 0;
			}
		} catch (Exception e) {
			logger.error("****Exception in getWelfareFundOfAnEmployee() " + e.getMessage());
			throw e;
		}
		return employeeWelfare;
	}

	/**
	 * @param SalaryProfessionaltaxSlabArr
	 */
	private float getProfessionalTaxOfAnEmployee(List<SalaryProfessionaltaxSlab> SalaryProfessionaltaxSlabArr,
			float totalSalaryOffered) throws Exception {
		float totalProffessionalTax = 0f;
		try {
			for (SalaryProfessionaltaxSlab objSalaryProfessionaltaxSlab : SalaryProfessionaltaxSlabArr) {

				if ((totalSalaryOffered) >= objSalaryProfessionaltaxSlab.getSlabamountstart()
						&& (totalSalaryOffered) <= objSalaryProfessionaltaxSlab.getSlabamountend()) {
					totalProffessionalTax = objSalaryProfessionaltaxSlab.getSlabamount();
					break;
				}
			}
		} catch (Exception e) {
			logger.error("****Exception in getProfessionalTaxOfAnEmployee() " + e.getMessage());
			throw e;
		}
		return totalProffessionalTax;
	}

	/**
	 * @param SalaryIncometaxSlabArr
	 * @param wpsOfOneEmployee
	 * @param totalSalaryOffered
	 */
	private float getIncomeTaxOfAnEmployee(List<SalaryIncometaxSlab> SalaryIncometaxSlabArr, float totalSalaryOffered)
			throws Exception {
		float taxableIncomePerMonth;
		float totalIncomeTax = 0f;
		// finding income tax
		try {
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
		} catch (Exception e) {
			logger.error("****Exception in getIncomeTaxOfAnEmployee() " + e.getMessage());
			throw e;
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
			EmployeeDetailsMaster employeeDetailsMasterObj) throws Exception {
		float halfPresentDays = 0f;
		try {
			for (Object[] halfPresentObj : userAttDetailsListHalfPresent) {

				if (("" + halfPresentObj[1]).equals("" + employeeDetailsMasterObj.getEmployeeBioDeviceCode())) {
					halfPresentDays = Float.parseFloat("" + halfPresentObj[0]);
					break;
				}

			}
		} catch (Exception e) {
			logger.error("****Exception in getHalfOrFullPresentDays() " + e.getMessage());
			throw e;
		}
		return halfPresentDays;
	}
	
	/**
	 * @param userAttDetailsListCasualLeave
	 * @param employeeDetailsMasterObj 
	 * @return
	 */
	private float getCasualLeaveDays(List<Object[]> userAttDetailsListCasualLeave,
			EmployeeDetailsMaster employeeDetailsMasterObj) throws Exception {
		float casualLeaveDays = 0f;
		try {
			for (Object[] casualLeaveObj : userAttDetailsListCasualLeave) {

				if (("" + casualLeaveObj[1]).equals("" + employeeDetailsMasterObj.getEmployeeBioDeviceCode())) {
					casualLeaveDays = Float.parseFloat("" + casualLeaveObj[0]);
					break;
				}

			}
		} catch (Exception e) {
			logger.error("****Exception in getCasualLeaveDays() " + e.getMessage());
			throw e;
		}
		return casualLeaveDays;
	}

	public float calculateSalary(Wps wpsObj, float totalSalaryOffered, float numberOfWorkingDays) throws Exception {
		float salary = 0f, totalPresentDays, totalIncomeTax, totalProfessionalTax, totalPF, totalESI, totalStaffAdvance,
				totalLineShort, totalSalaryAdvance, employeeWelfareAmount, totalEarnedThisMonth, totalDeductedThisMonth;
		try {
			totalPresentDays = wpsObj.getDaysOfAttandance();
			totalIncomeTax = wpsObj.getTotalIncomeTax();
			totalProfessionalTax = wpsObj.getTotalProfessionalTax();
			totalPF = wpsObj.getTotalPF();
			totalESI = wpsObj.getTotalESI();
			totalStaffAdvance = wpsObj.getTotalStaffAdvance();
			totalLineShort = wpsObj.getTotalLineShort();
			totalSalaryAdvance = wpsObj.getTotalSalaryAdvance();
			employeeWelfareAmount = wpsObj.getTotalEmployeeWelfareFund();

			totalEarnedThisMonth = totalSalaryOffered * totalPresentDays / numberOfWorkingDays;
			totalDeductedThisMonth = totalIncomeTax + totalProfessionalTax + totalPF + totalESI + totalStaffAdvance
					+ totalLineShort + totalSalaryAdvance + employeeWelfareAmount;
			wpsObj.setTotalDeduction(totalDeductedThisMonth);
			salary = totalEarnedThisMonth - totalDeductedThisMonth;
			
		} catch (Exception e) {
			logger.error("****Exception in calculateSalary() " + e.getMessage());
			throw e;
		}

		return salary;
	}

	public int hasSalaryGenerated(String year, String month) throws Exception {
		int returnValue = 0;
		try {
			returnValue = salaryStatusRepository.isSalaryGenerated(Integer.parseInt(year), Integer.parseInt(month));
		} catch (Exception e) {
			logger.error("****Exception in hasSalaryGenerated() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	public WritableWorkbook genereteExcel(int year, int month, HttpServletResponse response) throws Exception {
		String fileName = "Salary_Wps_" + year + "_" + month + ".xls";
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
		Integer[] array = { 0, 1, 2, 3, 4, 5, 7, 10, 11, 12, 13, 17, 18, 21, 30, 41, 42 };
		List<Integer> redFontIndex = Arrays.asList(array);

		try {
			List<Wps> returnValue = wpsRepository.findForCurrentMonth(year, month);
			Object[][] salaryExcel = new Object[returnValue.size()][heading.length];

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			workbook = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet excelOutputsheet = workbook.createSheet("Salary", 0);
			addExcelOutputHeader(excelOutputsheet, heading, redFontIndex);
			writeExcelOutputData(excelOutputsheet, returnValue, heading);

			workbook.write();
			workbook.close();

		} catch (FileNotFoundException e) {
			logger.error("****Exception in genereteExcel() " + e.getMessage());
			throw e;
		} catch (IOException e) {
			logger.error("****Exception in genereteExcel() " + e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("****Exception in genereteExcel() " + e.getMessage());
			throw e;
		}
		return workbook;

	}

	private void addExcelOutputHeader(WritableSheet sheet, String[] heading,List<Integer> redFontIndex)
			throws RowsExceededException, WriteException {
		for (int cell = 0; cell < heading.length; cell++) {
			sheet.setColumnView(cell, 20);
			sheet.addCell(new Label(cell, 0, heading[cell],getCellFormat(redFontIndex.contains(cell)?Colour.RED:Colour.BLACK, Pattern.GRAY_25)));
		}

	}

	private static WritableCellFormat getCellFormat(Colour colour, Pattern pattern) throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 16);
		cellFont.setPointSize(10);
		cellFont.setColour(colour);
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		return cellFormat;
	}

	private void writeExcelOutputData(WritableSheet sheet, List<Wps> returnValue, String[] heading) throws Exception {
		int rowNo = 0, colNo = 0;
		try {
			for (Wps excelRow : returnValue) {
				++rowNo;
				colNo = 0;
				sheet.addCell(new Label(colNo++, rowNo, excelRow.getEmployeeCode()));

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getEmployeeName()));
				// cell.setCellValue(excelRow.getEmployeeName());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getNameOfGuardian()));
				// cell.setCellValue(excelRow.getNameOfGuardian());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getEmployeeSex()));
				// cell.setCellValue(excelRow.getEmployeeSex());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDateOfBirth().toString()));
				// cell.setCellValue(excelRow.getDateOfBirth());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDesignation()));
				// cell.setCellValue(excelRow.getDesignation());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDesignationCode()));
				// cell.setCellValue(excelRow.getDesignationCode());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDateOfJoining().toString()));
				// cell.setCellValue(excelRow.getDateOfJoining());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getMobileNumber()));
				// cell.setCellValue(excelRow.getMobileNumber());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getEmailId()));
				// cell.setCellValue(excelRow.getEmailId());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getBankName()));
				// cell.setCellValue(excelRow.getBankName());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getIfscCode()));
				// cell.setCellValue(excelRow.getIfscCode());
				
				sheet.addCell(new Label(colNo++, rowNo, excelRow.getBankAccountNumber(),getCellFormat(Colour.RED, Pattern.GRAY_25)));
				// cell.setCellValue(excelRow.getBankAccountNumber());
				
				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDaysOfAttandance()+""));

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getLossOfPayDays() + ""));
				// cell.setCellValue(excelRow.getLossOfPayDays());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getNumberOfWeeklyOffGranted() + ""));
				// cell.setCellValue(excelRow.getNumberOfWeeklyOffGranted());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getNumberOfLeaveGranted() + ""));
				// cell.setCellValue(excelRow.getNumberOfLeaveGranted());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getBasic() + ""));
				// cell.setCellValue(excelRow.getBasic());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDa() + ""));
				// cell.setCellValue(excelRow.getDa());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getHra() + ""));
				// cell.setCellValue(excelRow.getHra());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getCityCompensationAllowence() + ""));
				// cell.setCellValue(excelRow.getCityCompensationAllowence());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getGrossMonthlyWages() + ""));
				// cell.setCellValue(excelRow.getGrossMonthlyWages());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getOverTimeWages() + ""));
				// cell.setCellValue(excelRow.getOverTimeWages());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getLeaveWages() + ""));
				// cell.setCellValue(excelRow.getLeaveWages());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getNationalAndFestivalHolidayWages() + ""));
				// cell.setCellValue(excelRow.getNationalAndFestivalHolidayWages());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getArrearPaid() + ""));
				// cell.setCellValue(excelRow.getArrearPaid());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getBonus() + ""));
				// cell.setCellValue(excelRow.getBonus());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getMaternityBenefit() + ""));
				// cell.setCellValue(excelRow.getMaternityBenefit());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getOtherAllowances() + ""));
				// cell.setCellValue(excelRow.getOtherAllowances());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalStaffAdvance() + ""));
				// cell.setCellValue(excelRow.getTotalStaffAdvance());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalSalaryForThisMonth()+""));// Total Amount
				// cell.setCellValue("");

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalPF()+""));// Provident Fund
				// cell.setCellValue("");// Provident Fund

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalESI()+""));/// State INsurance
				// cell.setCellValue("");// State INsurance

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalStaffAdvance() + ""));
				// cell.setCellValue(excelRow.getTotalStaffAdvance());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalEmployeeWelfareFund() + ""));
				// cell.setCellValue(excelRow.getTotalEmployeeWelfareFund());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalProfessionalTax() + ""));
				// cell.setCellValue(excelRow.getTotalProfessionalTax());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTaxDeductedAtSource() + ""));
				// cell.setCellValue(excelRow.getTaxDeductedAtSource());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDeductionOfFine() + ""));
				// cell.setCellValue(excelRow.getDeductionOfFine());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDeductionForLossAndDamages() + ""));
				// cell.setCellValue(excelRow.getDeductionForLossAndDamages());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getOtherDeduction() + ""));
				// cell.setCellValue(excelRow.getOtherDeduction());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalDeduction() + ""));
				// cell.setCellValue(excelRow.getTotalDeduction());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getNetWagesPaid() + ""));
				// cell.setCellValue(excelRow.getNetWagesPaid());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDateOfPayment() + ""));
				// cell.setCellValue(excelRow.getDateOfPayment());

				sheet.addCell(new Label(colNo++, rowNo, ""));// Remarks
				// cell.setCellValue("");// REMARKSss
			}
		} catch (Exception e) {
			logger.error("****Exception in writeExcelOutputData() " + e.getMessage());
			throw e;
		}

	}

}
