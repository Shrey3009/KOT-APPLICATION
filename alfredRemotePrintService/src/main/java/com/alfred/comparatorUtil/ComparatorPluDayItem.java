package com.alfred.comparatorUtil;

import java.util.Comparator;

import com.alfredbase.javabean.ReportPluDayItem;

public class ComparatorPluDayItem implements Comparator<ReportPluDayItem>{

	@Override
	public int compare(ReportPluDayItem arg0, ReportPluDayItem arg1) {
		return arg0.getItemMainCategoryId().compareTo(arg1.getItemMainCategoryId());
	}

}
