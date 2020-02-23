package com.alfred.comparatorUtil;

import java.util.Comparator;

import com.alfredbase.javabean.ReportPluDayComboModifier;

public class ComparatorPluDayComboModifier implements Comparator<ReportPluDayComboModifier>{

	@Override
	public int compare(ReportPluDayComboModifier arg0,
			ReportPluDayComboModifier arg1) {
		return arg0.getItemId().compareTo(arg1.getItemId());
	}

}
