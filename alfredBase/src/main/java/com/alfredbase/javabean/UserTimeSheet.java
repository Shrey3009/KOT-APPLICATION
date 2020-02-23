package com.alfredbase.javabean;

public class UserTimeSheet {
	private Integer id;
	private Long businessDate;
	private Integer restaurantId;
	private Integer revenueId;
	private Integer userId;
	private Integer empId;
	private String empName;
	private Long loginTime;
	private Long logoutTime;
	private Double totalHours;
	// 0 已登录未登出， 1已登录登出
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(Long businessDate) {
		this.businessDate = businessDate;
	}
	public Integer getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}
	public Integer getRevenueId() {
		return revenueId;
	}
	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public Long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Long loginTime) {
		this.loginTime = loginTime;
	}
	public Long getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Long logoutTime) {
		this.logoutTime = logoutTime;
	}
	public Double getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "UserTimeSheet [id=" + id + ", businessDate=" + businessDate
				+ ", restaurantId=" + restaurantId + ", revenueId=" + revenueId
				+ ", userId=" + userId + ", empId=" + empId + ", empName="
				+ empName + ", loginTime=" + loginTime + ", logoutTime="
				+ logoutTime + ", totalHours=" + totalHours + ", status="
				+ status + "]";
	}
	
}
