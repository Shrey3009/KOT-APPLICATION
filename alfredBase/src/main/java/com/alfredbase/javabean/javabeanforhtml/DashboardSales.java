package com.alfredbase.javabean.javabeanforhtml;

import android.text.TextUtils;

public class DashboardSales {
	private int userId;
	private String waiterName;
	private String sales;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getWaiterName() {
		return waiterName;
	}

	public void setWaiterName(String waiterName) {
		this.waiterName = waiterName;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		if (TextUtils.isEmpty(sales)) {
			this.sales = "0.00";
		} else {
			this.sales = sales;
		}
	}

	@Override
	public String toString() {
		return "DashboardSales [userId=" + userId + ", waiterName="
				+ waiterName + ", sales=" + sales + "]";
	}

}
