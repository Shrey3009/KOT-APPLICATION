package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

public class Tax {
	private Integer id;

	private Integer companyId;

	private Integer restaurantId;

	private String taxName;
	/**
	 * 税收比例
	 */
	private String taxPercentage;

	/**
	 * 税收类型(0消费税，1服务税)
	 */
	private Integer taxType;
	/**
	 * -1删除，0禁用，1正常
	 */
	private Integer status;

	private Long createTime;

	private Long updateTime;
     //	0 No  1 Yes
	private  Integer beforeDiscount;

	public Tax() {
	}

	public Tax(Integer id, Integer companyId, Integer restaurantId,
			String taxName, String taxPercentage, Integer taxType,
			Integer status, Long createTime, Long updateTime,Integer beforeDiscount) {
		super();
		this.id = id;
		this.companyId = companyId;
		this.restaurantId = restaurantId;
		this.taxName = taxName;
		this.taxPercentage = taxPercentage;
		this.taxType = taxType;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.beforeDiscount=beforeDiscount;
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

	public Integer getRestaurantId() {
		if (CommonUtil.isNull(restaurantId))
			return 0;
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getTaxName() {
		if (CommonUtil.isNull(taxName))
			return "";
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName == null ? null : taxName.trim();
	}

	public String getTaxPercentage() {
		if (CommonUtil.isNull(taxPercentage))
			return "";
		return taxPercentage;
	}

	public void setTaxPercentage(String taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Integer getTaxType() {
		if (CommonUtil.isNull(taxType))
			return 0;
		return taxType;
	}

	public void setTaxType(Integer taxType) {
		this.taxType = taxType;
	}

	public Integer getStatus() {
		if (CommonUtil.isNull(status))
			return 0;
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getBeforeDiscount() {
		return beforeDiscount;
	}

	public void setBeforeDiscount(Integer beforeDiscount) {
		this.beforeDiscount = beforeDiscount;
	}

	@Override
	public String toString() {
		return "Tax{" +
				"id=" + id +
				", companyId=" + companyId +
				", restaurantId=" + restaurantId +
				", taxName='" + taxName + '\'' +
				", taxPercentage='" + taxPercentage + '\'' +
				", taxType=" + taxType +
				", status=" + status +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", beforeDiscount=" + beforeDiscount +
				'}';
	}

}