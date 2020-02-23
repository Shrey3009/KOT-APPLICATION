package com.alfredbase;


import android.util.Log;

import java.util.Locale;

public class ParamHelper {
	public static String getPrintOrderNO(int orderNO) {
		String preStr = String.format(Locale.US,"%06d", orderNO);
		return preStr;
	}

	public static String getPrintOrderBillNo(int prefix, int orderBillNo) {
		String preStr = String.format(Locale.US,"%02d%06d",prefix,orderBillNo);
		return preStr;
	}
	
	public static String getPrintOrderBillNo(int orderBillNo) {
		String preStr = String.format(Locale.US,"%08d",orderBillNo);
		return preStr;
	}
}
