 package com.alfredbase.javabean;

import java.io.Serializable;

import com.alfredbase.utils.CommonUtil;

public class Restaurant implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1996436942750125478L;

	private Integer id;

	private Integer companyId;

	private String restaurantName;

	/**
	 * 餐厅类型(1餐厅，2咖啡店，3披萨，4夜店)
	 */
	private Integer type;
	/**
	 * 餐厅状态(-1删除，0禁用，1正常)
	 */
	private Integer status;

	private String description;

	private String email;

	private String address1;

	private String address2;

	private String telNo;

	private String country;

	private String state;

	private String city;

	private String postalCode;

	private Long createTime;

	private Long updateTime;
	
	private String website;
	
	private String addressPrint;	//	餐厅的打印地址
	
	private String logoUrl;	//	logo的下载路径
	
	private Integer qrPayment;	//	是否有二维码哦 0 否 1 是
	
	private String restaurantPrint;	 //	餐厅打印名称
	
	private String options; // customized fields shown at receipt header
	
	private String footerOptions; //customized fields shown at receipt footer
	private Integer reportOrderTimely;//即时报表

	private String RoundingFormart;

	public Restaurant() {
	}

	public Restaurant(Integer id, Integer companyId, String restaurantName,
			Integer type, Integer status, String description, String email,
			String address1, String address2, String telNo, String country,
			String state, String city, String postalCode, Long createTime,
			Long updateTime, String website, String addressPrint,
			String logoUrl, Integer qrPayment, String restaurantPrint,
			String options, String footerOptions) {
		super();
		this.id = id;
		this.companyId = companyId;
		this.restaurantName = restaurantName;
		this.type = type;
		this.status = status;
		this.description = description;
		this.email = email;
		this.address1 = address1;
		this.address2 = address2;
		this.telNo = telNo;
		this.country = country;
		this.state = state;
		this.city = city;
		this.postalCode = postalCode;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.website = website;
		this.addressPrint = addressPrint;
		this.logoUrl = logoUrl;
		this.qrPayment = qrPayment;
		this.restaurantPrint = restaurantPrint;
		this.options = options;
		this.footerOptions = footerOptions;
	}

	public String getFooterOptions() {
		return footerOptions;
	}

	public void setFooterOptions(String footerOptions) {
		this.footerOptions = footerOptions;
	}

	public Integer getId() {
		if (CommonUtil.isNull(id))
			return 0;
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		if (CommonUtil.isNull(companyId))
			return 0;
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getRestaurantName() {
		if (CommonUtil.isNull(restaurantName))
			return "";
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName == null ? null : restaurantName
				.trim();
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

	public String getDescription() {
		if (CommonUtil.isNull(description))
			return "";
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public String getEmail() {
		if (CommonUtil.isNull(email))
			return "";
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
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
	
	

	public String getWebsite() {
		if (CommonUtil.isNull(website))
			return "";
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAddressPrint() {
		return addressPrint;
	}

	public void setAddressPrint(String addressPrint) {
		this.addressPrint = addressPrint;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Integer getQrPayment() {
		return qrPayment;
	}

	public void setQrPayment(Integer qrPayment) {
		this.qrPayment = qrPayment;
	}

	public String getRestaurantPrint() {
		return restaurantPrint;
	}

	public void setRestaurantPrint(String restaurantPrint) {
		this.restaurantPrint = restaurantPrint;
	}

	public String getRoundingFormart() {
		return RoundingFormart;
	}

	public void setRoundingFormart(String roundingFormart) {
		RoundingFormart = roundingFormart;
	}

	public Integer getReportOrderTimely() {
		return reportOrderTimely;
	}

	public void setReportOrderTimely(Integer reportOrderTimely) {
		this.reportOrderTimely = reportOrderTimely;
	}

	@Override
	public String toString() {
		return "Restaurant{" +
				"id=" + id +
				", companyId=" + companyId +
				", restaurantName='" + restaurantName + '\'' +
				", type=" + type +
				", status=" + status +
				", description='" + description + '\'' +
				", email='" + email + '\'' +
				", address1='" + address1 + '\'' +
				", address2='" + address2 + '\'' +
				", telNo='" + telNo + '\'' +
				", country='" + country + '\'' +
				", state='" + state + '\'' +
				", city='" + city + '\'' +
				", postalCode='" + postalCode + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", website='" + website + '\'' +
				", addressPrint='" + addressPrint + '\'' +
				", logoUrl='" + logoUrl + '\'' +
				", qrPayment=" + qrPayment +
				", restaurantPrint='" + restaurantPrint + '\'' +
				", options='" + options + '\'' +
				", footerOptions='" + footerOptions + '\'' +
				", reportOrderTimely=" + reportOrderTimely +
				", RoundingFormart='" + RoundingFormart + '\'' +
				'}';
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

}