package com.alfredbase.javabean;

import java.io.Serializable;

public class MonthlySalesReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7606202488802034719L;
	private double totalSales;
	private double itemSales;
	private int personQty;
	private long businessDate;
	
	public double getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}
	public double getItemSales() {
		return itemSales;
	}
	public void setItemSales(double itemSales) {
		this.itemSales = itemSales;
	}
	public int getPersonQty() {
		return personQty;
	}
	public void setPersonQty(int personQty) {
		this.personQty = personQty;
	}
	public long getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(long businessDate) {
		this.businessDate = businessDate;
	}
	
	@Override
	public String toString() {
		return "MonthlySaleReport [totalSales=" + totalSales + ", itemSales="
				+ itemSales + ", personQty=" + personQty + ", businessDate="
				+ businessDate + "]";
	}
	
	
	
}
