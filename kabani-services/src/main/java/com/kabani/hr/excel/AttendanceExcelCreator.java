package com.kabani.hr.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kabani.hr.helper.DaysInYearAndMonthCalculator;

@Component
public class AttendanceExcelCreator {
	
	private static final String FILE_NAME = "C:/PERSONAL/Projects/fr/kabani/salary-mail-data/attendancesalarydetails/ho_wps_template.xlsx";
	@Autowired
	private DaysInYearAndMonthCalculator daysInYearAndMonthCalculator;
	
	public int createAttendanceExcelForMonthAndYear(int year,int month) {
		try {
		//Read the spreadsheet that needs to be updated
		FileInputStream fsIP= new FileInputStream(new File(FILE_NAME)); 
        
		//Access the workbook  
        XSSFWorkbook workbook = new XSSFWorkbook(fsIP);
      //Access the worksheet, so that we can update / modify it. 
        XSSFSheet sheet = workbook.getSheetAt(0); 
        List<String> dayNamesInMonth=daysInYearAndMonthCalculator.getAllDayNameInMonthAndYear(year, month); 
        Object[][] datatypes = {
                {"Datatype", "Type", "Size(in bytes)"},
                {"int", "Primitive", 2},
                {"float", "Primitive", 4},
                {"double", "Primitive", 8},
                {"char", "Primitive", 1},
                {"String", "Non-Primitive", "No fixed size"}
        };
        Object[][] salaryExcel = new Object[5][dayNamesInMonth.size()];
        int i=0;
        for (String day : dayNamesInMonth) {
        	salaryExcel[1][i]=Integer.parseInt(day.split("-")[0]);
        	salaryExcel[2][i]=day.split("-")[1];
        	i++;
		}
        

        int rowNum = 0;
        System.out.println("Creating excel");

//        for (Object[] datatype : salaryExcel) {
//            Row row = sheet.createRow(rowNum++);
//            int colNum = 0;
//            for (Object field : datatype) {
//                Cell cell = row.createCell(colNum++);
//                if (field instanceof String) {
//                    cell.setCellValue((String) field);
//                } else if (field instanceof Integer) {
//                    cell.setCellValue((Integer) field);
//                }
//            }
//        }
        
        Cell cell = null; 
     // Access the second cell in second row to update the value
        // row starts from 0 to total number of employees
        // cols start from 0 to 43
        for (int h=0;h<44;h++) {
        	cell = sheet.getRow(2).getCell(h);
        	  cell.setCellValue("h=="+h);
        }
        //upto zero to  43
     // Get current cell value value and overwrite the value
   

        
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
            fsIP.close(); 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       return 0;
    }

}
