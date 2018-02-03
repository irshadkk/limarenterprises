package com.kabani.hr.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kabani.hr.entity.EmployeeDetailsMaster;
import com.kabani.hr.entity.SalaryIncometaxSlab;
import com.kabani.hr.entity.SalaryProfessionaltaxSlab;
import com.kabani.hr.entity.Wps;
import com.kabani.hr.repository.EmployeeDetailsMasterRepository;
import com.kabani.hr.repository.HolidayDetailsMasterRepository;
import com.kabani.hr.repository.SalaryIncometaxSlabRepository;
import com.kabani.hr.repository.SalaryProfessionaltaxSlabRepository;
import com.kabani.hr.repository.UserAttendanceDetailsRepository;
import com.kabani.hr.repository.WpsRepository;

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

	public List calculateSalaryOfEmployees(String year, String month) {
		String[] monthArry = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		int indexOfMonth = Arrays.asList(monthArry).indexOf(month);
		int yr = Integer.parseInt(year);
		System.out.println("------------------------------------------");
		System.out.println("-" + year + "-" + month);
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
				wpsOfOneEmployee.setLossOfPayDays(numberOfWorkingDays-totalPresentDays);

				// finding salary offered by the company
				totalSalaryOffered = employeeDetailsMasterObj.getSalary();
				wpsOfOneEmployee.setGrossMonthlyWages(totalSalaryOffered);
				wpsOfOneEmployee.setTotalSalaryOffered(totalSalaryOffered);
				
				
				// finding income tax
				totalIncomeTax=getIncomeTaxOfAnEmployee(SalaryIncometaxSlabArr, totalSalaryOffered);
				wpsOfOneEmployee.setTotalIncomeTax(totalIncomeTax);
				wpsOfOneEmployee.setTaxDeductedAtSource(totalIncomeTax); 
				
				
				// finding professional tax
				totalProffessionalTax=getProfessionalTaxOfAnEmployee(SalaryProfessionaltaxSlabArr, totalSalaryOffered);
				wpsOfOneEmployee.setTotalProfessionalTax(totalProffessionalTax);

				// employee welfare fund
				wpsOfOneEmployee.setTotalEmployeeWelfareFund(getWelfareFundOfAnEmployee(employeeDetailsMasterObj, totalSalaryOffered,
						employeeWelfareAmount));

				// pf only for those who have salary less than 15000 12 % of offered salary
				totalPF=getPFOfanEmployee(totalSalaryOffered);
				wpsOfOneEmployee.setTotalPF(totalPF);

				// esi is applicable for all and reducing 1% for this year
				totalESI=getESIOfanEmployee(totalSalaryOffered);
				wpsOfOneEmployee.setTotalESI(totalESI);

				// calculating salary
				float salary = calculateSalary(wpsOfOneEmployee,totalSalaryOffered,numberOfWorkingDays);
				wpsOfOneEmployee.setNetWagesPaid(salary);
				wpsOfOneEmployee.setTotalSalaryForThisMonth(salary);
				
				result.add(wpsOfOneEmployee);

			}
			

		}
		wpsRepository.save(result); 
		 
		return result;
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
		float totalPF=0f;
		if (totalSalaryOffered < 15000) { 
			totalPF=(totalSalaryOffered * 12 / 100);
		} else {
			totalPF=0;
		}
		return totalPF;
	}

	/**
	 * @param employeeDetailsMasterObj
	 * @param wpsOfOneEmployee
	 * @param totalSalaryOffered
	 * @param employeeWelfareAmount
	 */
	private float getWelfareFundOfAnEmployee(EmployeeDetailsMaster employeeDetailsMasterObj,  
			float totalSalaryOffered, float employeeWelfareAmount) {
		float employeeWelfare=0f;
		if (employeeDetailsMasterObj.getEmployeeAge() < 55 && totalSalaryOffered >= 15000) {
			employeeWelfare=employeeWelfareAmount;
		} else {
			employeeWelfare=0;
		}
		return employeeWelfare;
	}

	/**
	 * @param SalaryProfessionaltaxSlabArr
	 */
	private float getProfessionalTaxOfAnEmployee(List<SalaryProfessionaltaxSlab> SalaryProfessionaltaxSlabArr,float totalSalaryOffered) {
		float totalProffessionalTax=0f;
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
		float totalIncomeTax=0f;
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

	public float calculateSalary(Wps wpsObj,float totalSalaryOffered,float numberOfWorkingDays) {
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
		float totalDeductedThisMonth = totalIncomeTax + totalProfessionalTax +totalPF+ totalESI + totalStaffAdvance
				+ totalLineShort + totalSalaryAdvance + employeeWelfareAmount;
		salary = totalEarnedThisMonth - totalDeductedThisMonth;

		return salary;
	}

}
