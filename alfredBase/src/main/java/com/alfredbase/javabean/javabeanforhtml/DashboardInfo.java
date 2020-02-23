package com.alfredbase.javabean.javabeanforhtml;

import java.util.ArrayList;

import com.alfredbase.utils.BH;

public class DashboardInfo {
	
	private ArrayList<DashboardTotalDetailInfo> totalDetailInfos;
	private String cash;
	private String cards;
	private String others;
	private String totalChecks;
	private int breakfast;
	private int lunch;
	private int dinner;
	private int totalOrders;
	private ArrayList<DashboardItemDetail> itemList;
	private int itemSum;
	private ArrayList<DashboardItemDetail> categoryItemList;
	private ArrayList<DashboardSales> waiterAndSales;

	public ArrayList<DashboardTotalDetailInfo> getTotalDetailInfos() {
		return totalDetailInfos;
	}

	public void setTotalDetailInfos(
			ArrayList<DashboardTotalDetailInfo> totalDetailInfos) {
		this.totalDetailInfos = totalDetailInfos;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
			this.cash = BH.getBD(cash).toString();
	}

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
			this.cards = BH.getBD(cards).toString();
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
			this.others = BH.getBD(others).toString();
	}

	public String getTotalChecks() {
		return totalChecks;
	}

	public void setTotalChecks(String totalChecks) {
		this.totalChecks = totalChecks;
	}

	public int getBreakfast() {
		return breakfast;
	}

	public void setBreakfast(int breakfast) {
		this.breakfast = breakfast;
	}

	public int getLunch() {
		return lunch;
	}

	public void setLunch(int lunch) {
		this.lunch = lunch;
	}

	public int getDinner() {
		return dinner;
	}

	public void setDinner(int dinner) {
		this.dinner = dinner;
	}

	public int getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}

	public ArrayList<DashboardItemDetail> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<DashboardItemDetail> itemList) {
		this.itemList = itemList;
	}

	public ArrayList<DashboardItemDetail> getCategoryItemList() {
		return categoryItemList;
	}

	public void setCategoryItemList(
			ArrayList<DashboardItemDetail> categoryItemList) {
		this.categoryItemList = categoryItemList;
	}

	public ArrayList<DashboardSales> getWaiterAndSales() {
		return waiterAndSales;
	}

	public void setWaiterAndSales(ArrayList<DashboardSales> waiterAndSales) {
		this.waiterAndSales = waiterAndSales;
	}
	
	public int getItemSum() {
		return itemSum;
	}

	public void setItemSum(int itemSum) {
		this.itemSum = itemSum;
	}

	@Override
	public String toString() {
		return "DashboardInfo [totalDetailInfos=" + totalDetailInfos
				+ ", cash=" + cash + ", cards=" + cards + ", others=" + others
				+ ", totalChecks=" + totalChecks + ", breakfast=" + breakfast
				+ ", lunch=" + lunch + ", dinner=" + dinner + ", totalOrders="
				+ totalOrders + ", itemList=" + itemList + ", itemSum="
				+ itemSum + ", categoryItemList=" + categoryItemList
				+ ", waiterAndSales=" + waiterAndSales + "]";
	}
}
