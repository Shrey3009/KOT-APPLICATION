package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2178463169657077870L;

	private Integer id;

	private Integer empId;
	/**
	 * 账户类型(10服务员，11厨房，12收银员，13 大堂经理，20后台用户，21企业管理员，999系统管理员)
	 */
	private Integer type;
	/**
	 * 账户状态(-1删除，0禁用，1正常)
	 */
	private Integer status;

	private String accountName;

	private String userName;

	private String password;

	private String firstName;

	private String lastName;

	private String nickName;

	private Integer companyId;

	private Long createTime;

	private Long updateTime;

	public User() {
	}

	public User(Integer id, Integer empId, Integer type, Integer status,
			String accountName, String userName, String password,
			String firstName, String lastName, String nickName,
			Integer companyId, Long createTime, Long updateTime) {
		super();
		this.id = id;
		this.empId = empId;
		this.type = type;
		this.status = status;
		this.accountName = accountName;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.companyId = companyId;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Integer getId() {
		if (CommonUtil.isNull(id))
			return 0;
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmpId() {
		if (CommonUtil.isNull(empId))
			return 0;
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Integer getType() {
		if (CommonUtil.isNull(type))
			return 0;
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		if (CommonUtil.isNull(status))
			return 0;
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAccountName() {
		if (CommonUtil.isNull(accountName))
			return "";
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getUserName() {
		if (CommonUtil.isNull(userName))
			return "";
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		if (CommonUtil.isNull(password))
			return "";
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		if (CommonUtil.isNull(firstName))
			return "";
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		if (CommonUtil.isNull(lastName))
			return "";
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		if (CommonUtil.isNull(nickName))
			return "";
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getCompanyId() {
		if (CommonUtil.isNull(companyId))
			return 0;
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Long getCreateTime() {
		if (CommonUtil.isNull(createTime))
			return 0L;
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		if (CommonUtil.isNull(updateTime))
			return 0L;
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", empId=" + empId + ", type=" + type
				+ ", status=" + status + ", accountName=" + accountName
				+ ", userName=" + userName + ", password=" + password
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", nickName=" + nickName + ", companyId=" + companyId
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}
}