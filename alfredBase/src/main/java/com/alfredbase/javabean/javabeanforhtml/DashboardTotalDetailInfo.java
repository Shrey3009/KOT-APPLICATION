package com.alfredbase.javabean.javabeanforhtml;

import com.alfredbase.utils.BH;

public class DashboardTotalDetailInfo {
	private String subTotal;
	private String totalTax;
	private String totalDiscount;
	private String totalAmount;
	private long businessDateStr;

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
			this.subTotal = BH.getBD(subTotal).toString();
	}

	public String getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(String totalTax) {
			this.totalTax = BH.getBD(totalTax).toString();
	}

	public String getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(String totalDiscount) {
			this.totalDiscount = BH.getBD(totalDiscount).toString();
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
			this.totalAmount = BH.getBD(totalAmount).toString();
	}

	public long getBusinessDateStr() {
		return businessDateStr;
	}

	public void setBusinessDateStr(long businessDateStr) {
		this.businessDateStr = businessDateStr;
	}

	@Override
	public String toString() {
		return "TotalDetailInfo [subTotal=" + subTotal + ", totalTax="
				+ totalTax + ", totalDiscount=" + totalDiscount
				+ ", totalAmount=" + totalAmount + ", businessDateStr="
				+ businessDateStr + "]";
	}
}
