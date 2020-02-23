package com.alfredbase.javabean;

public class CashInOut {
	private int id;
	private int restaurantId;
	private int revenueId;
	private int userId;
	private int empId;
	private String empName;
	private long businessDate;
	private int type;	//0-放钱，1-拿钱 2-启动资金
	private String comment;
	private String cash;	//钱的金额
	private String createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}
	public int getRevenueId() {
		return revenueId;
	}
	public void setRevenueId(int revenueId) {
		this.revenueId = revenueId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public long getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(long businessDate) {
		this.businessDate = businessDate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "CashInOut [id=" + id + ", restaurantId=" + restaurantId
				+ ", revenueId=" + revenueId + ", userId=" + userId
				+ ", empId=" + empId + ", empName=" + empName
				+ ", businessDate=" + businessDate + ", type=" + type
				+ ", comment=" + comment + ", cash=" + cash + ", createTime="
				+ createTime + "]";
	}
	
}
