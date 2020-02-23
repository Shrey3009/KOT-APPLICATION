package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

import java.io.Serializable;

public class ItemMainCategory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6115444304691292165L;

	private Integer id;

	private String mainCategoryName;

	private String color;

	private Integer restaurantId;
	/**
	 * 是否可用(-1删除，0禁用，1正常)
	 */
	private Integer isActive;

	private Integer indexId;

	private Integer userId;

	private Integer printerGroupId;

	private Long createTime;

	private Long updateTime;

	private int isShowDiner;

	public ItemMainCategory() {
	}

	public ItemMainCategory(Integer id, String mainCategoryName, String color,
			Integer restaurantId, Integer isActive, Integer indexId,
			Integer userId, Integer printerGroupId, Long createTime,
			Long updateTime) {
		super();
		this.id = id;
		this.mainCategoryName = mainCategoryName;
		this.color = color;
		this.restaurantId = restaurantId;
		this.isActive = isActive;
		this.indexId = indexId;
		this.userId = userId;
		this.printerGroupId = printerGroupId;
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

	public String getMainCategoryName() {
		if (CommonUtil.isNull(mainCategoryName))
			return "";
		return mainCategoryName;
	}

	public void setMainCategoryName(String mainCategoryName) {
		this.mainCategoryName = mainCategoryName == null ? null
				: mainCategoryName.trim();
	}

	public String getColor() {
		if (CommonUtil.isNull(color))
			return "";
		return color;
	}

	public void setColor(String color) {
		this.color = color == null ? null : color.trim();
	}

	public Integer getRestaurantId() {
		if (CommonUtil.isNull(restaurantId))
			return 0;
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Integer getIsActive() {
		if (CommonUtil.isNull(isActive))
			return 0;
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIndexId() {
		if (CommonUtil.isNull(indexId))
			return 0;
		return indexId;
	}

	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}

	public Integer getUserId() {
		if (CommonUtil.isNull(userId))
			return 0;
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Integer getPrinterGroupId() {
		if (CommonUtil.isNull(printerGroupId))
			return 0;
		return printerGroupId;
	}

	public void setPrinterGroupId(Integer printerGroupId) {
		this.printerGroupId = printerGroupId;
	}

	public int getIsShowDiner() {
		return isShowDiner;
	}

	public void setIsShowDiner(int isShowDiner) {
		this.isShowDiner = isShowDiner;
	}

	@Override
	public String toString() {
		return "ItemMainCategory{" +
				"id=" + id +
				", mainCategoryName='" + mainCategoryName + '\'' +
				", color='" + color + '\'' +
				", restaurantId=" + restaurantId +
				", isActive=" + isActive +
				", indexId=" + indexId +
				", userId=" + userId +
				", printerGroupId=" + printerGroupId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", isShowDiner=" + isShowDiner +
				'}';
	}

}