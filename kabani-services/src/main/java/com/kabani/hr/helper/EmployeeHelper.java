package com.kabani.hr.helper;

import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kabani.hr.entity.EmployeeDetailsMaster;
import com.kabani.hr.repository.EmployeeDetailsMasterRepository;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

@Component
public class EmployeeHelper {
	private final Logger logger = LogManager.getLogger(this.getClass());
	@Autowired
	private EmployeeDetailsMasterRepository employeeDetailsMasterRepository;

	public WritableWorkbook genereteExcel(String filter, HttpServletResponse response) throws Exception {
		String filterType = filter.substring(0, filter.indexOf("::"));
		String filterValue = filter.substring(filter.indexOf("::") + 2);

		String name = "%";
		String branch = "%";
		String designation = "%";
		String department = "%";
		if (filterType.equals("name")) {
			name = filterValue;
		}
		if (filterType.equals("designation")) {
			designation = filterValue;
		}
		if (filterType.equals("branch")) {
			branch = filterValue;
		}
		if (filterType.equals("department")) {
			department = filterValue;
		}
		String fileName = "Employee_" + filterType + "_" + filterValue + ".xls";
		WritableWorkbook workbook = null;
		try {
			List<EmployeeDetailsMaster> returnValue = employeeDetailsMasterRepository.filterEmployees(name, branch,
					department, designation);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

			workbook = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet excelOutputsheet = workbook.createSheet("Employees", 0);
			if (returnValue.size() > 0) {
				writeExcelOutputData(excelOutputsheet, returnValue);
			}

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

	private static WritableCellFormat getCellFormat(Colour colour, Pattern pattern) throws WriteException {
		WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 16);
		cellFont.setPointSize(10);
		cellFont.setColour(colour);
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		return cellFormat;
	}

	private void writeExcelOutputData(WritableSheet sheet, List<EmployeeDetailsMaster> returnValue) throws Exception {
		int rowNo = 0, colNo = 0;
		Class aClass = returnValue.get(0).getClass();

		Field[] fields = aClass.getDeclaredFields();

		for (int cell = 0; cell < fields.length; cell++) {
			sheet.setColumnView(cell, 20);
			sheet.addCell(new Label(cell, 0, fields[cell].getName(), getCellFormat(Colour.RED, Pattern.GRAY_25)));
		}
		try {
			for (EmployeeDetailsMaster excelRow : returnValue) {
				++rowNo;
				aClass = excelRow.getClass();

				fields = aClass.getDeclaredFields();

				for (int cell = 0; cell < fields.length; cell++) {
					sheet.setColumnView(cell, 20);
					sheet.addCell(new Label(cell, rowNo, ""
							+ new PropertyDescriptor(fields[cell].getName(), aClass).getReadMethod().invoke(excelRow)));
				}
			}

		} catch (Exception e) {
			logger.error("****Exception in writeExcelOutputData() " + e.getMessage());
			throw e;
		}

	}

}
