package com.alfredposclient.global;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;

import com.alfredbase.utils.IntegerUtils;
import com.alfredposclient.R;
public class PreciseTimeUtil {
	private Activity context;
	int year;
	int month;
	int day;
	int week;
	int hour;
	int min;

	public PreciseTimeUtil(Activity context) {
		this.context = context;
		Calendar rightNow = Calendar.getInstance(Locale.US);
		year = rightNow.get(Calendar.YEAR);
		month = rightNow.get(Calendar.MONTH);
		day = rightNow.get(Calendar.DAY_OF_MONTH);
		week = rightNow.get(Calendar.DAY_OF_WEEK);
		hour = rightNow.get(Calendar.HOUR_OF_DAY);
		min = rightNow.get(Calendar.MINUTE);
	}
	public String getYear() {
		return Integer.toString(year);
	}

	public String getMonth() {
		return context.getResources().getStringArray(R.array.month_array)[month];
	}

	public String getDay() {
		return IntegerUtils.ordinal(day);
	}

	public String getWeek() {
		return context.getResources().getStringArray(R.array.week_array)[week];
	}

	public String getHour() {
		return hour < 10 ? "0" + Integer.toString(hour) : Integer
				.toString(hour);
	}

	public String getMin() {
		return min < 10 ? "0" + Integer.toString(min) : Integer.toString(min);
	}
}
