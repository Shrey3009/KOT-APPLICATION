package com.alfredbase.javabean;

public class EmpWorkLog {
	private Integer id;
	private Integer userId;
	private Integer empId;
	private String empName;
	private Long loginTime;
	private Long logoutTime;
	private Integer totalHours;
	private Integer status;

	public EmpWorkLog() {
	}

	public EmpWorkLog(Integer id, Integer userId, Integer empId,
			String empName, Long loginTime, Long logoutTime,
			Integer totalHours, Integer status) {
		super();
		this.id = id;
		this.userId = userId;
		this.empId = empId;
		this.empName = empName;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.totalHours = totalHours;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Integer totalHours) {
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
		return "EmpWorkLog [id=" + id + ", userId=" + userId + ", empId="
				+ empId + ", empName=" + empName + ", loginTime=" + loginTime
				+ ", logoutTime=" + logoutTime + ", totalHours=" + totalHours
				+ ", status=" + status + "]";
	}
}
