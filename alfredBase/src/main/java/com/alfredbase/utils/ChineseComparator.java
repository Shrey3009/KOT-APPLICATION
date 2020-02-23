package com.alfredbase.utils;

import java.util.Comparator;

import com.alfredbase.javabean.ItemDetail;

public class ChineseComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		String str1 = CommonUtil.getInitial(((ItemDetail) o1).getItemName());
		String str2 = CommonUtil.getInitial(((ItemDetail) o2).getItemName());
		return str1.compareTo(str2);
	}

}
