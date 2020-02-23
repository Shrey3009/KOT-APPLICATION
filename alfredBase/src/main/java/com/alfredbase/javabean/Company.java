package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

public class Company {
	private Integer id;

	private Integer userId;
	private String companyName;

	private String email;

	/**
	 * 0free，1普通，2高级
	 */
	private Integer level;

	private String address1;

	private String address2;

	private String telNo;

	private String country;

	private String state;

	private String city;

	private String postalCode;

	private Long createTime;

	private Long updateTime;

	public Company() {
	}

	public Company(Integer id, Integer userId, String companyName,
			String email, Integer level, String address1, String address2,
			String telNo, String country, String state, String city,
			String postalCode, Long createTime, Long updateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.companyName = companyName;
		this.email = email;
		this.level = level;
		this.address1 = address1;
		this.address2 = address2;
		this.telNo = telNo;
		this.country = country;
		this.state = state;
		this.city = city;
		this.postalCode = postalCode;
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

	public Integer getUserId() {
		if (CommonUtil.isNull(userId))
			return 0;
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCompanyName() {
		if (CommonUtil.isNull(companyName))
			return "";
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName == null ? null : companyName.trim();
	}

	public String getEmail() {
		if (CommonUtil.isNull(email))
			return "";
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public Integer getLevel() {
		if (CommonUtil.isNull(level))
			return 0;
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getAddress1() {
		if (CommonUtil.isNull(address1))
			return "";
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1 == null ? null : address1.trim();
	}

	public String getAddress2() {
		if (CommonUtil.isNull(address2))
			return "";
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2 == null ? null : address2.trim();
	}

	public String getTelNo() {
		if (CommonUtil.isNull(telNo))
			return "";
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo == null ? null : telNo.trim();
	}

	public String getCountry() {
		if (CommonUtil.isNull(country))
			return "";
		return country;
	}

	public void setCountry(String country) {
		this.country = country == null ? null : country.trim();
	}

	public String getState() {
		if (CommonUtil.isNull(state))
			return "";
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	public String getCity() {
		if (CommonUtil.isNull(city))
			return "";
		return city;
	}

	public void setCity(String city) {
		this.city = city == null ? null : city.trim();
	}

	public String getPostalCode() {
		if (CommonUtil.isNull(postalCode))
			return "";
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode == null ? null : postalCode.trim();
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
		return "Company [id=" + id + ", userId=" + userId + ", companyName="
				+ companyName + ", email=" + email + ", level=" + level
				+ ", address1=" + address1 + ", address2=" + address2
				+ ", telNo=" + telNo + ", country=" + country + ", state="
				+ state + ", city=" + city + ", postalCode=" + postalCode
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}
}