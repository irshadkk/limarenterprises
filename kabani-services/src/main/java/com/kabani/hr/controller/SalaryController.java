package com.kabani.hr.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kabani.hr.entity.EmployeeLoan;
import com.kabani.hr.entity.EmployeeLoanorAdvanceDeduction;
import com.kabani.hr.entity.SalaryStatus;
import com.kabani.hr.entity.UserAttendanceDetails;
import com.kabani.hr.entity.Wps;
import com.kabani.hr.helper.ExcelOutputServiceImpl;
import com.kabani.hr.helper.SalaryCalculator;
import com.kabani.hr.repository.EmployeeLoanRepository;
import com.kabani.hr.repository.EmployeeLoanorAdvanceDeductionRepository;
import com.kabani.hr.repository.SalaryStatusRepository;
import com.kabani.hr.repository.UserAttendanceDetailsRepository;
import com.kabani.hr.repository.WpsRepository;

@CrossOrigin
@Controller // This means that this class is a Controller
@RequestMapping(path = "/salary") // This means URL's start with /demo (after
// Application path)
public class SalaryController {
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private SalaryCalculator salaryCalculator;

	@Autowired
	private SalaryStatusRepository salaryStatusRepository;
	@Autowired
	private WpsRepository wpsRepository;
	@Autowired
	private ExcelOutputServiceImpl excelOutputServiceImpl;
	@Autowired
	private UserAttendanceDetailsRepository userAttendanceDetailsRepository;
	@Autowired
	private EmployeeLoanRepository employeeLoanRepository;
	@Autowired
	private EmployeeLoanorAdvanceDeductionRepository employeeLoanorAdvanceDeductionRepository;

	@RequestMapping(value = "/salaryGenerated", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody int getSalaryStatus(@RequestParam String year, @RequestParam String month) throws Exception {
		int response = 0;
		try {
			response = salaryCalculator.hasSalaryGenerated(year, month);
		} catch (Exception e) {
			logger.error("****Exception in getSalaryStatus() " + e.getMessage());
			throw e;
		}
		return response;
	}

	@PostMapping(path = "/generateSalary/{year}/{month}")
	public @ResponseBody List generateSalary(@PathVariable String year, @PathVariable String month) throws Exception {
		List returnValue = new ArrayList<>();
		try {
			returnValue = salaryCalculator.calculateSalaryOfEmployees(year, month);
		} catch (Exception e) {
			logger.error("****Exception in generateSalary() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	@RequestMapping(value = "/getSalary", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List getSalary(@RequestParam String year, @RequestParam String month) throws Exception {
		List returnValue = new ArrayList<>();
		try {
			returnValue = salaryCalculator.getSalary(Integer.parseInt(year), Integer.parseInt(month));
		} catch (Exception e) {
			logger.error("****Exception in getSalary() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	@RequestMapping(value = "/getSalaryExcel", method = RequestMethod.GET)
	public ModelAndView getSalaryExel(@RequestParam String year, @RequestParam String month,
			HttpServletResponse response) throws Exception {
		try {
			salaryCalculator.genereteExcel(Integer.parseInt(year), Integer.parseInt(month), response);
		} catch (Exception e) {
			logger.error("****Exception in getSalaryExel() " + e.getMessage());
			throw e;
		}
		return null;
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ModelAndView downloadExcelOutputExl(HttpServletResponse response) throws Exception {
		try {
			excelOutputServiceImpl.createExcelOutputExcel(response);
		} catch (Exception e) {
			logger.error("****Exception in downloadExcelOutputExl() " + e.getMessage());
			throw e;
		}
		return null;
	}

	@RequestMapping(value = "/salaryStatusAll", method = RequestMethod.GET)
	public @ResponseBody List getSalaryStatusAll() throws Exception {
		List<SalaryStatus> result;
		try {
			result = salaryStatusRepository.findAll();
		} catch (Exception e) {
			logger.error("****Exception in getSalaryStatusAll() " + e.getMessage());
			throw e;
		}
		return result;
	}

	@RequestMapping(value = "/salaryStatusRemove", method = RequestMethod.POST)
	public @ResponseBody boolean salaryStatusRemove(@RequestBody SalaryStatus salaryStatus) throws Exception {
		try {
			salaryStatusRepository.delete(salaryStatus);
		} catch (Exception e) {
			logger.error("****Exception in getSalaryStatusAll() " + e.getMessage());
			throw e;
		}
		return true;
	}

	@RequestMapping(value = "/salaryStatusResetAll", method = RequestMethod.GET)
	public @ResponseBody String salaryStatusRemove() throws Exception {
		String result = "true";
		List<SalaryStatus> salSt = salaryStatusRepository.findAll();
		List<Wps> wpsA = wpsRepository.findAll();
		try {
			for (SalaryStatus salaryStatus : salSt) {
				salaryStatusRepository.delete(salaryStatus);

			}
			for (Wps wps : wpsA) {
				wpsRepository.delete(wps);

			}
			for (UserAttendanceDetails wps : userAttendanceDetailsRepository.findAll()) {
				userAttendanceDetailsRepository.delete(wps);

			}

		} catch (Exception e) {
			logger.error("****Exception in getSalaryStatusAll() " + e.getMessage());
			throw e;
		}
		return result;
	}

	@RequestMapping(value = "/getActiveLoans", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List getActiveLoans(@RequestParam String year, @RequestParam String month) throws Exception {
		List returnValue = new ArrayList<>();
		try {
			List<EmployeeLoanorAdvanceDeduction> activeEmi = employeeLoanorAdvanceDeductionRepository
					.getActiveLoanDeductionForMonth(Integer.parseInt(month), Integer.parseInt(year));

			if (activeEmi.size() > 0) {
				ArrayList<Integer> loanIdList = new ArrayList<Integer>();
				for (EmployeeLoanorAdvanceDeduction temp : activeEmi) {
					loanIdList.add(temp.getLoanId());
				}
				returnValue = employeeLoanRepository.getActiveLoanForMonth(loanIdList);
			}
		} catch (Exception e) {
			logger.error("****Exception in getActiveLoans() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	@RequestMapping(value = "/addNewLoan", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody EmployeeLoan addNewLoan(@RequestBody EmployeeLoan loan) throws Exception {
		EmployeeLoan returnValue = null;
		try {
			returnValue = employeeLoanRepository.save(loan);
			for (int count = 0; count < returnValue.getLoanTenure(); count++) {
				Date temp = returnValue.getAvailDate();
				LocalDate futureDate = temp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(count);
				employeeLoanorAdvanceDeductionRepository.save(
						new EmployeeLoanorAdvanceDeduction(returnValue.getEmployeeCode(), returnValue.getEmployeeName(),
								returnValue.getId(), "Loan", returnValue.getLoanAmount() / returnValue.getLoanTenure(),
								Date.from(futureDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), "unpaid",
								(count == (returnValue.getLoanTenure() - 1)) ? true : false));
			}
		} catch (Exception e) {
			logger.error("****Exception in addNewLoan() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	@RequestMapping(value = "/closeLoan", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody EmployeeLoan closeLoan(@RequestBody EmployeeLoan loan) throws Exception {
		EmployeeLoan returnValue = null;
		try {
			returnValue = employeeLoanRepository.save(loan);

			for (EmployeeLoanorAdvanceDeduction installment : employeeLoanorAdvanceDeductionRepository
					.getAllInstallmentForLoan(loan.getId())) {
				installment.setStatus("paid");
				employeeLoanorAdvanceDeductionRepository.save(installment);
			}
		} catch (Exception e) {
			logger.error("****Exception in closeLoan() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

	@RequestMapping(value = "/getActiveAdvances", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List getActiveAdvances(@RequestParam String year, @RequestParam String month)
			throws Exception {
		List returnValue = new ArrayList<>();
		try {
			return employeeLoanorAdvanceDeductionRepository
					.getActiveLoanDeductionForMonth(Integer.parseInt(month), Integer.parseInt(year));

			
		} catch (Exception e) {
			logger.error("****Exception in getActiveAdvancesv() " + e.getMessage());
			throw e;
		}
	
	}

	@RequestMapping(value = "/addNewAdvance", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody EmployeeLoanorAdvanceDeduction addNewAdvance(@RequestBody EmployeeLoanorAdvanceDeduction loan)
			throws Exception {
		EmployeeLoanorAdvanceDeduction returnValue = null;
		try {
			returnValue = employeeLoanorAdvanceDeductionRepository.save(loan);
		} catch (Exception e) {
			logger.error("****Exception in addNewAdvance() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}
	
	@RequestMapping(value = "/addNewLoanEmi", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody EmployeeLoanorAdvanceDeduction addNewLoanEmi(@RequestBody EmployeeLoanorAdvanceDeduction loan)
			throws Exception {
		EmployeeLoanorAdvanceDeduction returnValue = null;
		try {
			returnValue = employeeLoanorAdvanceDeductionRepository.save(loan);
		} catch (Exception e) {
			logger.error("****Exception in addNewLoanEmi() " + e.getMessage());
			throw e;
		}
		return returnValue;
	}

}
