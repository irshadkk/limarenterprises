package com.kabani.hr.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.springframework.transaction.annotation.Transactional;

import com.kabani.hr.entity.EmployeeDetailsMaster;
import com.kabani.hr.entity.EmployeeIncentive;
import com.kabani.hr.entity.EmployeeIncomeTaxDetailsMaster;
import com.kabani.hr.entity.EmployeeLoan;
import com.kabani.hr.entity.EmployeeLoanorAdvanceDeduction;
import com.kabani.hr.entity.EmployeeOvertimeWages;
import com.kabani.hr.entity.SalaryIncometaxSlab;
import com.kabani.hr.entity.SalaryProfessionaltaxSlab;
import com.kabani.hr.entity.SalaryStatus;
import com.kabani.hr.entity.Wps;
import com.kabani.hr.repository.EmployeeDetailsMasterRepository;
import com.kabani.hr.repository.EmployeeIncentiveRepository;
import com.kabani.hr.repository.EmployeeIncomeTaxDetailsMasterRepository;
import com.kabani.hr.repository.EmployeeLoanRepository;
import com.kabani.hr.repository.EmployeeLoanorAdvanceDeductionRepository;
import com.kabani.hr.repository.EmployeeOvertimeWagesRepository;
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
@Transactional(rollbackFor = Exception.class)
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

	@Autowired
	private EmployeeLoanRepository employeeLoanRepository;

	@Autowired
	private EmployeeLoanorAdvanceDeductionRepository employeeLoanorAdvanceDeductionRepository;

	@Autowired
	private EmployeeIncomeTaxDetailsMasterRepository employeeIncomeTaxDetailsMasterRepository;

	@Autowired
	private EmployeeIncentiveRepository employeeIncentiveRepository;

	@Autowired
	private EmployeeOvertimeWagesRepository employeeOvertimeWagesRepository;

	public List<Wps> getSalary(int year, int month, String type) {
		return wpsRepository.findForCurrentMonth(year, month, type);
	}

	public List calculateSalaryOfEmployees(String year, String month) throws Exception {
		String[] monthArry = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		int indexOfMonth = Arrays.asList(monthArry).indexOf(month);
		int yr = Integer.parseInt(year);
		logger.info(
				"----------------calculateSalaryOfEmployees" + year + "--" + month + "-----START-------------------");
		int numberOfWorkingDays = 26;

		List<EmployeeLoanorAdvanceDeduction> salaryAdvances = new ArrayList<EmployeeLoanorAdvanceDeduction>();
		List<EmployeeLoanorAdvanceDeduction> staffAdvances = new ArrayList<EmployeeLoanorAdvanceDeduction>();
		List<EmployeeIncomeTaxDetailsMaster> incomeTaxArr = new ArrayList<EmployeeIncomeTaxDetailsMaster>();
		List<EmployeeIncentive> incentiveArr = new ArrayList<EmployeeIncentive>();
		List<EmployeeOvertimeWages> overTimeWages = new ArrayList<EmployeeOvertimeWages>();

		ArrayList<Wps> result;
		try {
			salaryAdvances = employeeLoanorAdvanceDeductionRepository.getActiveAdvancesForMonth(indexOfMonth + 1, yr);
			staffAdvances = employeeLoanorAdvanceDeductionRepository.getActiveLoanDeductionForMonth(indexOfMonth + 1,
					yr);
			incentiveArr = employeeIncentiveRepository.getIncentiveForMonth(indexOfMonth + 1, yr);
			incomeTaxArr = employeeIncomeTaxDetailsMasterRepository.getTaxForMonthYearForEmployee(indexOfMonth + 1, yr);
			overTimeWages = employeeOvertimeWagesRepository.getOvertimeWagesForMonth(indexOfMonth + 1, yr);

			int numberOfDaysInMonthYear = daysInYearAndMonthCalculator.calculateDaysInMonthAndYear(yr,
					indexOfMonth + 1);
			int numberOfHolidaysInMonthYear = holidayDetailsMasterRepository.findCountOfHolidayByYearMonth(yr,
					indexOfMonth + 1);
			int numberOfActualWorkingDays = numberOfDaysInMonthYear - numberOfHolidaysInMonthYear;
			int numberOfDaysToAddWithPresentDaysOfEmployeeFor26 = (numberOfActualWorkingDays != 26)
					? (26 - numberOfActualWorkingDays)
					: 0;

			result = new ArrayList();
			/* Updating Employee table to set WelfareFund eligibility to 0 if age >45 */
			employeeDetailsMasterRepository.updateWelfareFundEligiblity();

			// getting all employees details
			List<EmployeeDetailsMaster> allEmployeeMasterDetailsLst = employeeDetailsMasterRepository.findAll();

			// getting all employees full present for this month and year
			List<Object[]> userAttDetailsListFullPresent = userAttendanceDetailsRepository
					.findDistinctPresentInMonthYear(indexOfMonth + 1, yr);
			// getting all employees half present for this month and year
			List<Object[]> userAttDetailsListHalfPresent = userAttendanceDetailsRepository
					.findDistinctHalfPresentInMonthYear(indexOfMonth + 1, yr);
			// getting all employees casual leaves for this month and year
			List<Object[]> userAttDetailsListCasualLeave = userAttendanceDetailsRepository
					.findDistinctCasualLeaveInMonthYear(indexOfMonth + 1, yr);
			// getting salary incometax slab
			List<SalaryIncometaxSlab> SalaryIncometaxSlabArr = salaryIncometaxSlabRepository.findAll();
			// getting salary professionaltax slab
			List<SalaryProfessionaltaxSlab> SalaryProfessionaltaxSlabArr = salaryProfessionaltaxSlabRepository
					.findAll();

			if ((null != userAttDetailsListFullPresent && userAttDetailsListFullPresent.size() > 0)
					|| (null != userAttDetailsListHalfPresent && userAttDetailsListHalfPresent.size() > 0)
					|| (null != userAttDetailsListCasualLeave && userAttDetailsListCasualLeave.size() > 0)) {
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
							* (((fullPresentDays) + (halfPresentDays / 2) + (casualLeavesTaken))
									/ numberOfActualWorkingDays);
					int per26AddingDays = (int) Math.round(percentageOfNumberOfDaysToAddWithPresentDaysOfEmployeeFor26);
					totalPresentDays = fullPresentDays + (halfPresentDays / 2) + casualLeavesTaken + per26AddingDays;

					if (totalPresentDays > 0) {// only generating salary if employee is present atleast once in a month
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
						wpsOfOneEmployee.setTotalSalaryAdvance(0);
						wpsOfOneEmployee.setAdvanceTotalAmount(0);
						copyDataFromMaster(wpsOfOneEmployee, employeeDetailsMasterObj);
						identifyAdvances(wpsOfOneEmployee, employeeDetailsMasterObj.getEmployeeBioDeviceCode(),
								salaryAdvances, staffAdvances, incentiveArr,overTimeWages);
						wpsOfOneEmployee.setNumberOfWeeklyOffGranted(numberOfHolidaysInMonthYear);
						wpsOfOneEmployee.setEmployeeCode(employeeDetailsMasterObj.getEmployeeCode());
						wpsOfOneEmployee.setEmployeeName(employeeDetailsMasterObj.getEmployeeName());
						wpsOfOneEmployee.setHalfPresentDays(halfPresentDays);
						wpsOfOneEmployee.setFullPresentDays(fullPresentDays);
						wpsOfOneEmployee.setTotalCasualLeavesDays(casualLeavesTaken);
						/*
						 * employeeDetailsMasterObj.setCasualLeavesRemaining(
						 * employeeDetailsMasterObj.getCasualLeavesRemaining() - casualLeavesTaken);
						 * employeeDetailsMasterRepository.save(employeeDetailsMasterObj);
						 */

						wpsOfOneEmployee.setTotalPresentDaysPlus26PercentageAddition(totalPresentDays);

						wpsOfOneEmployee.setTotalPresentDays(totalPresentDays);
						// setting totalPresentDays after all additions
						wpsOfOneEmployee.setDaysOfAttandance(totalPresentDays);
						wpsOfOneEmployee.setLossOfPayDays(numberOfWorkingDays - totalPresentDays);

						// finding salary offered by the company
						totalSalaryOffered = employeeDetailsMasterObj.getBasic() + employeeDetailsMasterObj.getDa()
								+ employeeDetailsMasterObj.getHra()
								+ employeeDetailsMasterObj.getCityCompensationAllowence();

						wpsOfOneEmployee.setTotalSalaryOffered(totalSalaryOffered);

						// finding income tax
						// totalIncomeTax = getIncomeTaxOfAnEmployee(SalaryIncometaxSlabArr,
						// totalSalaryOffered);
						totalIncomeTax = getIncomeTaxOfAnEmployeeForFinanceYear2017(employeeDetailsMasterObj,
								incomeTaxArr);
						wpsOfOneEmployee.setTotalIncomeTax(totalIncomeTax * 12);
						wpsOfOneEmployee.setTaxDeductedAtSource(totalIncomeTax);

						// finding professional tax after checking whether he is eligible for it or not
						if (!employeeDetailsMasterObj.isNotElibibleForProfessionalTax()) {
							totalProffessionalTax = getProfessionalTaxOfAnEmployee(SalaryProfessionaltaxSlabArr,
									totalSalaryOffered);

						} else {
							totalProffessionalTax = 0;
						}

						wpsOfOneEmployee.setTotalProfessionalTax(totalProffessionalTax);

						// employee welfare fund
						wpsOfOneEmployee.setTotalEmployeeWelfareFund(getWelfareFundOfAnEmployee(
								employeeDetailsMasterObj, totalSalaryOffered, employeeWelfareAmount));

						// pf only for those who have salary less than 15000 12 % of offered salary
						totalPF = getPFOfanEmployee(employeeDetailsMasterObj, totalSalaryOffered);
						wpsOfOneEmployee.setTotalPF(totalPF);

						// esi is applicable for all and reducing 1% for this year
						totalESI = getESIOfanEmployee(totalSalaryOffered);
						wpsOfOneEmployee.setTotalESI(totalESI);

						// calculating salary

						calculateSalary(wpsOfOneEmployee, totalSalaryOffered, numberOfWorkingDays);

						wpsOfOneEmployee.setDateOfPayment(
								Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
						wpsOfOneEmployee.setYear(Integer.parseInt(year));
						wpsOfOneEmployee.setMonth(indexOfMonth + 1);

						result.add(wpsOfOneEmployee);
					}

				}

			}

			wpsRepository.save(result);
			employeeDetailsMasterRepository.updateWelfareFundEligiblity();
			salaryStatusRepository.save(new SalaryStatus(indexOfMonth + 1, Integer.parseInt(year),
					Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), "Full Month"));
			/* Updating Advances */
			for (EmployeeLoanorAdvanceDeduction salaryAdvance : salaryAdvances) {
				salaryAdvance.setStatus("paid");
				// employeeLoanorAdvanceDeductionRepository.save(salaryAdvance);
			}
			/* Updating Loan */
			for (EmployeeLoanorAdvanceDeduction staffAdvance : staffAdvances) {
				staffAdvance.setStatus("paid");
				// employeeLoanorAdvanceDeductionRepository.save(staffAdvance);
				if (staffAdvance.isLast()) {
					// Close Loan if last Installment
					EmployeeLoan temp = employeeLoanRepository.getLoanInfo(staffAdvance.getEmployeeCode(),
							staffAdvance.getLoanId());
					temp.setStatus("Closed");
					employeeLoanRepository.save(temp);
				}
			}
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

	private void identifyAdvances(Wps wps, String biometricCode, List<EmployeeLoanorAdvanceDeduction> salaryAdvances,
			List<EmployeeLoanorAdvanceDeduction> staffAdvances, List<EmployeeIncentive> incentives,
			List<EmployeeOvertimeWages> overtimeWages) {
		for (EmployeeLoanorAdvanceDeduction salaryAdvance : salaryAdvances) {
			if (salaryAdvance.getEmployeeCode().equals(biometricCode)) {
				wps.setAdvanceTotalAmount(wps.getAdvanceTotalAmount() + salaryAdvance.getAmount());
				break;
			}
		}
		for (EmployeeLoanorAdvanceDeduction staffAdvance : staffAdvances) {
			if (staffAdvance.getEmployeeCode().equals(biometricCode)) {
				wps.setTotalStaffAdvance(staffAdvance.getAmount());
				wps.setAdvanceTotalAmount(wps.getAdvanceTotalAmount() + staffAdvance.getAmount());
				break;
			}
		}
		for (EmployeeIncentive advance : incentives) {
			if (advance.getEmployeeCode().equals(wps.getEmployeeCode())) {
				wps.setTotalSalaryAdvance(advance.getAmount());
				//wps.setAdvanceTotalAmount(wps.getAdvanceTotalAmount() + advance.getAmount());
				break;
			}
		}
		for (EmployeeOvertimeWages overtimeWage : overtimeWages) {
			if (overtimeWage.getEmployeeCode().equals(biometricCode)) {
				wps.setOverTimeWages(wps.getOverTimeWages() + overtimeWage.getWage());
			}
		}

	}

	private void copyDataFromMaster(Wps wps, EmployeeDetailsMaster master) {
		wps.setBankAccountNumber(master.getBankAccountNumber());
		wps.setBankName(master.getBankName());
		wps.setBasic(master.getBasic());
		wps.setBranch(master.getBranch());
		wps.setCasualLeavesRemaining(master.getCasualLeavesRemaining());
		wps.setCasualLeavesTaken(master.getCasualLeavesTaken());
		wps.setCityCompensationAllowence(master.getCityCompensationAllowence());
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

		wps.setLeaveWages(master.getLeaveWages());
		wps.setNationalAndFestivalHolidayWages(master.getNationalAndFestivalHolidayWages());
		wps.setArrearPaid(master.getArrearPaid());
		wps.setBonus(master.getBonus());
		wps.setMaternityBenefit(master.getMaternityBenefit());
		wps.setOtherAllowances(master.getOtherAllowances());
		// wps.setTotalStaffAdvance(master.getTotalStaffAdvance());
		// wps.setTotalSalaryAdvance(master.getTotalSalaryAdvance());

		wps.setDeductionOfFine(master.getDeductionOfFine());
		wps.setDeductionForLossAndDamages(master.getDeductionForLossAndDamages());
		wps.setTotalLineShort(master.getTotalLineShort());
		wps.setOtherDeduction(master.getOtherDeduction());
		wps.setOtherAllowances(master.getOtherAllowances());
		wps.setType("Full Month");
	}

	/**
	 * @param totalSalaryOffered
	 * @return
	 */
	private float getESIOfanEmployee(float totalSalaryOffered) {
		float esi = 0f;
		if (totalSalaryOffered <= 21000) {
			esi = (totalSalaryOffered * 1) / 100;
		} else {
			esi = 0f;
		}
		return esi;
	}

	/**
	 * @param wpsOfOneEmployee
	 * @param totalSalaryOffered
	 */
	private float getPFOfanEmployee(EmployeeDetailsMaster employeeDetailsMasterObj, float totalSalaryOffered)
			throws Exception {
		float totalPF = 0f;
		try {
			if ((totalSalaryOffered < 15000) && (("LM/HO/053".equals(employeeDetailsMasterObj.getEmployeeCode()))
					|| ("LM/HO/054".equals(employeeDetailsMasterObj.getEmployeeCode()))
					|| ("LM/HO/055".equals(employeeDetailsMasterObj.getEmployeeCode())))) {
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
			employeeWelfare = employeeWelfareAmount;
			if ((employeeDetailsMasterObj.isNotElibibleForWelfareFund()) || totalSalaryOffered < 15000) {
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
	 * @param totalMonthlySalaryOffered
	 */
	private float getIncomeTaxOfAnEmployee(List<SalaryIncometaxSlab> SalaryIncometaxSlabArr,
			float totalMonthlySalaryOffered) throws Exception {

		float taxableIncomePerMonth;
		float incomeTaxPerMonth = 0f;
		float totalYearlySalary = totalMonthlySalaryOffered * 12;

		float tax = 0;
		try {
			// high income bracket
			if (totalYearlySalary > 1000000) {
				float part = totalYearlySalary - 1000000;
				tax += part * 0.3;
				totalYearlySalary = 1000000;
			}

			// medium income bracket
			if (totalYearlySalary > 500000) {
				float part = totalYearlySalary - 500000;
				tax += part * 0.2;
				totalYearlySalary = 500000;
			}

			if (totalYearlySalary > 300000) {
				float part = totalYearlySalary - 300000;
				tax += part * 0.05;
			}

			// tax for low income is zero, we don't need to compute anything.

		} catch (Exception e) {
			logger.error("****Exception in getIncomeTaxOfAnEmployee() " + e.getMessage());
			throw e;
		}
		return tax / 12;
	}

	/**
	 * @param SalaryIncometaxSlabArr
	 * @param wpsOfOneEmployee
	 * @param totalMonthlySalaryOffered
	 */
	private float getIncomeTaxOfAnEmployeeForFinanceYear2017(EmployeeDetailsMaster employeeDetailsMasterObj,
			List<EmployeeIncomeTaxDetailsMaster> incomeTaxArr) throws Exception {
		float tax = 0f;

		try {
			for (EmployeeIncomeTaxDetailsMaster employeeIncomeTaxDetailsMaster : incomeTaxArr) {
				if (employeeIncomeTaxDetailsMaster.getEmployeeCode()
						.equals(employeeDetailsMasterObj.getEmployeeCode())) {
					tax = employeeIncomeTaxDetailsMaster.getTaxAmount();
					break;
				}
			}

		} catch (Exception e) {
			logger.error("****Exception in getIncomeTaxOfAnEmployee() " + e.getMessage());
			throw e;
		}
		return tax;
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
		float salary = 0f, totalPresentDays, totalIncomeTax, totalProfessionalTax, totalPF, totalESI, totalLineShort,
				totalAdvance, employeeWelfareAmount, grossMonthlyWage, totalDeductedThisMonth, totalEarnedThisMonth;
		try {
			totalPresentDays = wpsObj.getDaysOfAttandance();
			totalIncomeTax = wpsObj.getTaxDeductedAtSource();
			totalProfessionalTax = wpsObj.getTotalProfessionalTax();
			totalPF = wpsObj.getTotalPF();
			totalESI = wpsObj.getTotalESI();
			wpsObj.setBasic(wpsObj.getBasic() * totalPresentDays / numberOfWorkingDays);
			wpsObj.setDa(wpsObj.getDa() * totalPresentDays / numberOfWorkingDays);
			wpsObj.setHra(wpsObj.getHra() * totalPresentDays / numberOfWorkingDays);
			wpsObj.setTotalSalaryOffered(wpsObj.getTotalSalaryOffered() * totalPresentDays / numberOfWorkingDays);
			// totalStaffAdvance = wpsObj.getTotalStaffAdvance();
			totalLineShort = wpsObj.getTotalLineShort();
			// totalSalaryAdvance = wpsObj.getTotalSalaryAdvance();
			totalAdvance = wpsObj.getAdvanceTotalAmount();
			employeeWelfareAmount = wpsObj.getTotalEmployeeWelfareFund();

			grossMonthlyWage = totalSalaryOffered * totalPresentDays / numberOfWorkingDays;
			wpsObj.setGrossMonthlyWages(grossMonthlyWage);
			totalDeductedThisMonth = totalPF + totalESI + totalAdvance + totalProfessionalTax + employeeWelfareAmount
					+ totalIncomeTax + wpsObj.getDeductionOfFine() + wpsObj.getDeductionForLossAndDamages()
					+ wpsObj.getOtherDeduction();

			wpsObj.setTotalDeduction(totalDeductedThisMonth);
			/*
			 * System.out.println("xxxxxxxxxxxxxxx--grossMonthlyWage>>"+grossMonthlyWage+
			 * "----");
			 * System.out.println("xxxxxxxxxxxxxxx--totalSalaryOffered>>"+totalSalaryOffered
			 * +"----");
			 * System.out.println("xxxxxxxxxxxxxxx--totalPresentDays>>"+totalPresentDays+
			 * "----"); System.out.println("xxxxxxxxxxxxxxx--numberOfWorkingDays>>"+
			 * numberOfWorkingDays+"----");
			 * System.out.println("xxxxxxxxxxxxxxx--wpsObj.getOverTimeWages() >>"+wpsObj.
			 * getOverTimeWages() +"----");
			 * System.out.println("xxxxxxxxxxxxxxx--wpsObj.getLeaveWages()>>"+wpsObj.
			 * getLeaveWages()+"----"); System.out.println(
			 * "xxxxxxxxxxxxxxx--wpsObj.getNationalAndFestivalHolidayWages()>>"+wpsObj.
			 * getNationalAndFestivalHolidayWages()+"----");
			 * System.out.println("xxxxxxxxxxxxxxx-- wpsObj.getBonus()>>"+
			 * wpsObj.getBonus()+"----");
			 * System.out.println("xxxxxxxxxxxxxxx-- wpsObj.getBonus()>>"+
			 * wpsObj.getBonus()+"----");
			 * System.out.println("xxxxxxxxxxxxxxx-- wpsObj.getBonus()>>"+
			 * wpsObj.getBonus()+"----");
			 */
			totalEarnedThisMonth = grossMonthlyWage + wpsObj.getOverTimeWages() + wpsObj.getLeaveWages()
					+ wpsObj.getNationalAndFestivalHolidayWages() + wpsObj.getArrearPaid() + wpsObj.getBonus()
					+ wpsObj.getMaternityBenefit() + wpsObj.getOtherAllowances();

			wpsObj.setTotalSalaryForThisMonth(totalEarnedThisMonth);

			/*
			 * if (wpsObj.getEmployeeName().equals("Bibin R")) { System.out.println(
			 * "SALARY==111>>totalSalaryOffered * totalPresentDays / numberOfWorkingDays " +
			 * totalSalaryOffered + "*" + totalPresentDays + "/" + numberOfWorkingDays +
			 * "======" + totalEarnedThisMonth);
			 * System.out.println("DEDUCTION==>>totalIncomeTax" + totalIncomeTax +
			 * " totalProfessionalTax " + totalProfessionalTax + " totalPF " + totalPF +
			 * " totalESI " + totalESI + " totalStaffAdvance " + totalStaffAdvance + "" +
			 * " totalLineShort " + totalLineShort + " totalSalaryAdvance " +
			 * totalSalaryAdvance + " employeeWelfareAmount " + employeeWelfareAmount +
			 * " wpsObj.getDeductionOfFine() " + wpsObj.getDeductionOfFine() + "" +
			 * " wpsObj.getDeductionForLossAndDamages() " +
			 * wpsObj.getDeductionForLossAndDamages() + " wpsObj.getOtherDeduction() " +
			 * wpsObj.getOtherDeduction() + "==========" + wpsObj.getTotalDeduction());
			 * 
			 * System.out.println("EARNINGS==>>grossMonthlyWage " + grossMonthlyWage +
			 * " wpsObj.getOverTimeWages() " + wpsObj.getOverTimeWages() +
			 * " wpsObj.getLeaveWages() " + wpsObj.getLeaveWages() +
			 * " wpsObj.getAdvanceTotalAmount() " + wpsObj.getAdvanceTotalAmount() +
			 * "========" + wpsObj.getTotalSalaryForThisMonth());
			 * 
			 * System.out.println("SALARY==22>>" + (totalEarnedThisMonth -
			 * totalDeductedThisMonth));
			 * 
			 * }
			 */
			salary = wpsObj.getTotalSalaryForThisMonth() - wpsObj.getTotalDeduction();
			wpsObj.setNetWagesPaid(salary);

		} catch (Exception e) {
			logger.error("****Exception in calculateSalary() " + e.getMessage());
			throw e;
		}

		return salary;
	}

	public int hasSalaryGenerated(String year, String month, String type) throws Exception {
		int returnValue = 0;
		try {
			returnValue = salaryStatusRepository.isSalaryGenerated(Integer.parseInt(year), Integer.parseInt(month),
					type);
		} catch (Exception e) {
			logger.error("****Exception in hasSalaryGenerated() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	public WritableWorkbook genereteExcel(int year, int month, String type, HttpServletResponse response)
			throws Exception {
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
			List<Wps> returnValue = wpsRepository.findForCurrentMonth(year, month, type);
			Object[][] salaryExcel = new Object[returnValue.size()][heading.length];

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			workbook = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet excelOutputsheet = workbook.createSheet("Salary", 0);
			addExcelOutputHeader(excelOutputsheet, heading, redFontIndex);
			writeExcelOutputData(excelOutputsheet, returnValue, heading, type);

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

	private void addExcelOutputHeader(WritableSheet sheet, String[] heading, List<Integer> redFontIndex)
			throws RowsExceededException, WriteException {
		for (int cell = 0; cell < heading.length; cell++) {
			sheet.setColumnView(cell, 20);
			sheet.addCell(new Label(cell, 0, heading[cell],
					getCellFormat(redFontIndex.contains(cell) ? Colour.RED : Colour.BLACK, Pattern.GRAY_25)));
		}
		for (int cell = 0; cell < heading.length; cell++) {
			sheet.setColumnView(cell, 20);
			sheet.addCell(new Label(cell, 1, (cell + 1) + "", getCellFormat(Colour.BLACK, Pattern.GRAY_25)));
		}

	}

	private static WritableCellFormat getCellFormat(Colour colour, Pattern pattern) throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 16);
		cellFont.setPointSize(10);
		cellFont.setColour(colour);
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		return cellFormat;
	}

	private String formatDate(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
		/* System.out.println("xxxxxxxxxx-->>"+DATE_FORMAT.format(date)); */
		return DATE_FORMAT.format(date);
	}

	private void writeExcelOutputData(WritableSheet sheet, List<Wps> returnValue, String[] heading, String type)
			throws Exception {
		int rowNo = 1, colNo = 0;
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

				sheet.addCell(new Label(colNo++, rowNo, formatDate(excelRow.getDateOfBirth())));
				// cell.setCellValue(excelRow.getDateOfBirth());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDesignation()));
				// cell.setCellValue(excelRow.getDesignation());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDesignationCode()));
				// cell.setCellValue(excelRow.getDesignationCode());

				sheet.addCell(new Label(colNo++, rowNo, formatDate(excelRow.getDateOfJoining())));
				// cell.setCellValue(excelRow.getDateOfJoining());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getMobileNumber()));
				// cell.setCellValue(excelRow.getMobileNumber());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getEmailId()));
				// cell.setCellValue(excelRow.getEmailId());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getBankName()));
				// cell.setCellValue(excelRow.getBankName());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getIfscCode()));
				// cell.setCellValue(excelRow.getIfscCode());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getBankAccountNumber(),
						getCellFormat(Colour.RED, Pattern.GRAY_25)));
				// cell.setCellValue(excelRow.getBankAccountNumber());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getDaysOfAttandance() + ""));

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getLossOfPayDays() + ""));
				// cell.setCellValue(excelRow.getLossOfPayDays());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getNumberOfWeeklyOffGranted() + ""));
				// cell.setCellValue(excelRow.getNumberOfWeeklyOffGranted());

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalCasualLeavesDays() + ""));

				if (type.equals("Full Month")) {
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getBasic() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getDa() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getHra() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getCityCompensationAllowence() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getGrossMonthlyWages() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getOverTimeWages() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getLeaveWages() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getNationalAndFestivalHolidayWages() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getArrearPaid() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getBonus() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getMaternityBenefit() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getOtherAllowances() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalSalaryAdvance() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalSalaryForThisMonth() + ""));
				} else if (type.equals("Half Month")) {
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, "0"));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalSalaryAdvance() + ""));
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalSalaryAdvance() + ""));
				}

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalPF() + ""));// Provident Fund
				// cell.setCellValue("");// Provident Fund

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalESI() + ""));/// State INsurance
				// cell.setCellValue("");// State INsurance

				sheet.addCell(new Label(colNo++, rowNo, excelRow.getAdvanceTotalAmount() + ""));
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

				if (type.equals("Full Month")) {
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getNetWagesPaid() + ""));
				} else if (type.equals("Half Month")) {
					sheet.addCell(new Label(colNo++, rowNo, excelRow.getTotalSalaryAdvance() + ""));
				}

				sheet.addCell(new Label(colNo++, rowNo, formatDate(excelRow.getDateOfPayment())));

				sheet.addCell(new Label(colNo++, rowNo, ""));// Remarks
				// cell.setCellValue("");// REMARKSss
			}
		} catch (Exception e) {
			logger.error("****Exception in writeExcelOutputData() " + e.getMessage());
			throw e;
		}

	}

	public List generateMidMonthSalary(String year, String month) throws Exception {
		String[] monthArry = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		int indexOfMonth = Arrays.asList(monthArry).indexOf(month);
		int yr = Integer.parseInt(year);
		int numberOfWorkingDays = 26;

		List<EmployeeLoanorAdvanceDeduction> salaryAdvances = new ArrayList<EmployeeLoanorAdvanceDeduction>();

		ArrayList<Wps> result;
		try {
			salaryAdvances = employeeLoanorAdvanceDeductionRepository.getActiveAdvancesForMonth(indexOfMonth + 1, yr);
			List<EmployeeDetailsMaster> allEmployeeMasterDetailsLst = employeeDetailsMasterRepository.findAll();
			result = new ArrayList();

			for (EmployeeLoanorAdvanceDeduction advance : salaryAdvances) {
				for (EmployeeDetailsMaster employeeDetailsMasterObj : allEmployeeMasterDetailsLst) {
					if (employeeDetailsMasterObj.getEmployeeBioDeviceCode().equals(advance.getEmployeeCode())) {
						Wps wpsOfOneEmployee = new Wps();
						copyDataFromMaster(wpsOfOneEmployee, employeeDetailsMasterObj);
						wpsOfOneEmployee.setEmployeeCode(employeeDetailsMasterObj.getEmployeeCode());
						wpsOfOneEmployee.setEmployeeName(employeeDetailsMasterObj.getEmployeeName());
						wpsOfOneEmployee.setTotalSalaryAdvance(advance.getAmount());
						wpsOfOneEmployee.setDateOfPayment(
								Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
						wpsOfOneEmployee.setYear(Integer.parseInt(year));
						wpsOfOneEmployee.setMonth(indexOfMonth + 1);
						wpsOfOneEmployee.setType("Half Month");
						result.add(wpsOfOneEmployee);
					}
				}
			}

			wpsRepository.save(result);
			salaryStatusRepository.save(new SalaryStatus(indexOfMonth + 1, Integer.parseInt(year),
					Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), "Half Month"));
			/* Updating Advances */
			for (EmployeeLoanorAdvanceDeduction salaryAdvance : salaryAdvances) {
				salaryAdvance.setStatus("paid");
			}

		} catch (NumberFormatException e) {
			logger.error("****Exception in generateMidMonthSalary() " + e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("****Exception in generateMidMonthSalary() " + e.getMessage());
			throw e;
		}
		return result;
	}

	public void resetForCurrentMonth(Integer year, Integer month, String type) throws Exception {
		System.out.println(year + "-----" + month + "-----" + type);
		try {
			List<Wps> returnValue = wpsRepository.findForCurrentMonth(year, month, type);
			for (Wps temp : returnValue) {
				wpsRepository.delete(temp);
			}
			if (type.equals("Half Month")) {
				List<EmployeeLoanorAdvanceDeduction> salaryAdvances = employeeLoanorAdvanceDeductionRepository
						.getDeductionForMonth(month, year, "paid", "advance");
				for (EmployeeLoanorAdvanceDeduction temp : salaryAdvances) {

					temp.setStatus("unpaid");
					employeeLoanorAdvanceDeductionRepository.save(temp);
				}
			}
			if (type.equals("Full Month")) {
				List<EmployeeLoanorAdvanceDeduction> salaryAdvances = employeeLoanorAdvanceDeductionRepository
						.getDeductionForMonth(month, year, "paid", "loan");
				for (EmployeeLoanorAdvanceDeduction temp : salaryAdvances) {
					temp.setStatus("unpaid");
					employeeLoanorAdvanceDeductionRepository.save(temp);
					if (temp.isLast()) {
						EmployeeLoan loan = employeeLoanRepository.getLoanInfo(temp.getEmployeeCode(),
								temp.getLoanId());
						loan.setStatus("active");
						employeeLoanRepository.save(loan);
					}
				}
			}
			for (SalaryStatus temp : salaryStatusRepository.salaryGeneratedForMonth(year, month, type)) {
				salaryStatusRepository.delete(temp);
			}

		} catch (NumberFormatException e) {
			logger.error("****Exception in generateMidMonthSalary() " + e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("****Exception in generateMidMonthSalary() " + e.getMessage());
			throw e;
		}

	}

}
