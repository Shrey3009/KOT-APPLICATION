package com.alfredbase.javabean;

import java.io.Serializable;

public class RevenueCenter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1933146222413189153L;

	private Integer id;

	private Integer restaurantId;

	private Integer printId;

	private String revName;
	/**
	 * 是否可用(-1删除，0禁用，1正常)
	 */
	private Integer isActive;

	private String createTime;

	private String updateTime;

	private Integer happyHourId;

	private Long happyStartTime;

	private Long happyEndTime;
	
	// 一个组合型数据 前端不用 01收银中心序号 001000当前编号
	private String currentBillNo;
	
	// currentBillNo 拆分的数据 表示收银中心序号
	private int indexId;
	
	/*
	 * currentBillNo 拆分的数据 表示当前编号 后台发过来的 BillNo后六位的起始数
	 * 前端规则 从后台获取值后 每次产生BillNO都需要更新此字段（+1）
	 */
	private int currentValue;
	
	private int isKiosk;

	/*
	 *	currentReportValue  销售报表的记录下标
	 */
	private int currentReportNo = 0;

	public RevenueCenter() {
	}

	public RevenueCenter(Integer id, Integer restaurantId, Integer printId,
			String revName, Integer isActive, String createTime,
			String updateTime, Integer happyHourId, Long happyStartTime,
			Long happyEndTime, String currentBillNo, int indexId, int currentValue, int isKiosk) {
		super();
		this.id = id;
		this.restaurantId = restaurantId;
		this.printId = printId;
		this.revName = revName;
		this.isActive = isActive;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.happyHourId = happyHourId;
		this.happyStartTime = happyStartTime;
		this.happyEndTime = happyEndTime;
		this.currentBillNo = currentBillNo;
		this.indexId = indexId;
		this.currentValue = currentValue;
		this.isKiosk = isKiosk;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Integer getPrintId() {
		return printId;
	}

	public void setPrintId(Integer printId) {
		this.printId = printId;
	}

	public String getRevName() {
		return revName;
	}

	public void setRevName(String revName) {
		this.revName = revName;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getHappyHourId() {
		return happyHourId;
	}

	public void setHappyHourId(Integer happyHourId) {
		this.happyHourId = happyHourId;
	}

	public Long getHappyStartTime() {
		return happyStartTime;
	}

	public void setHappyStartTime(Long happyStartTime) {
		this.happyStartTime = happyStartTime;
	}

	public Long getHappyEndTime() {
		return happyEndTime;
	}

	public void setHappyEndTime(Long happyEndTime) {
		this.happyEndTime = happyEndTime;
	}


	public String getCurrentBillNo() {
		return currentBillNo;
	}

	public void setCurrentBillNo(String currentBillNo) {
		this.currentBillNo = currentBillNo;
	}

	public int getIndexId() {
		return indexId;
	}

	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public int getIsKiosk() {
		return isKiosk;
	}

	public void setIsKiosk(int isKiosk) {
		this.isKiosk = isKiosk;
	}

	public int getCurrentReportNo() {
		return currentReportNo;
	}

	public void setCurrentReportNo(int currentReportNo) {
		this.currentReportNo = currentReportNo;
	}

	@Override
	public String toString() {
		return "RevenueCenter{" +
				"id=" + id +
				", restaurantId=" + restaurantId +
				", printId=" + printId +
				", revName='" + revName + '\'' +
				", isActive=" + isActive +
				", createTime='" + createTime + '\'' +
				", updateTime='" + updateTime + '\'' +
				", happyHourId=" + happyHourId +
				", happyStartTime=" + happyStartTime +
				", happyEndTime=" + happyEndTime +
				", currentBillNo='" + currentBillNo + '\'' +
				", indexId=" + indexId +
				", currentValue=" + currentValue +
				", isKiosk=" + isKiosk +
				", currentReportNo=" + currentReportNo +
				'}';
	}
}