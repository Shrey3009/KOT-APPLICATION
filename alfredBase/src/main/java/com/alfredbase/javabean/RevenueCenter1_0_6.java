package com.alfredbase.javabean;

import java.io.Serializable;

public class RevenueCenter1_0_6 implements Serializable {
	private static final long serialVersionUID = 5392951480519573423L;

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

	public RevenueCenter1_0_6() {
	}

	public RevenueCenter1_0_6(Integer id, Integer restaurantId, Integer printId,
			String revName, Integer isActive, String createTime,
			String updateTime, Integer happyHourId, Long happyStartTime,
			Long happyEndTime) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "RevenueCenter [id=" + id + ", restaurantId=" + restaurantId
				+ ", printId=" + printId + ", revName=" + revName
				+ ", isActive=" + isActive + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", happyHourId=" + happyHourId
				+ ", happyStartTime=" + happyStartTime + ", happyEndTime="
				+ happyEndTime + "]";
	}
}