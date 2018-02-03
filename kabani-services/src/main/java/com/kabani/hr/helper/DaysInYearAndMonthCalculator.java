package com.kabani.hr.helper;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DaysInYearAndMonthCalculator {

	public int calculateDaysInMonthAndYear(int year, int month) {
		YearMonth yearMonthObject = YearMonth.of(year, month);
		int daysInMonth = yearMonthObject.lengthOfMonth();
		return daysInMonth;

	}
 
	public List getAllDayNameInMonthAndYear(int year, int month) {
		List dateArr = new ArrayList();
		DateFormatSymbols dfs = new DateFormatSymbols();
		String weekdays[] = dfs.getWeekdays();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat df = new SimpleDateFormat("dd");
		for (int i = 1; i < maxDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i + 1);
			int day = cal.get(Calendar.DAY_OF_WEEK);
			String nameOfDay = weekdays[day];
			dateArr.add(df.format(cal.getTime())+"-"+nameOfDay.substring(0,3));

		}
		return dateArr;

	}

	 

}
