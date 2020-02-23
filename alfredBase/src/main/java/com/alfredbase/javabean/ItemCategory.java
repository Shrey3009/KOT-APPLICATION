package com.alfredbase.javabean;

import java.io.Serializable;

import com.alfredbase.utils.CommonUtil;

public class ItemCategory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6441915253558637759L;

	private Integer id;

	private String itemCategoryName;

	// 没用
	private Integer superCategoryId;

	private String color;

	private Integer itemMainCategoryId;

	private Integer restaurantId;
	/**
	 * 0禁用，1正常
	 */
	private Integer isActive;

	private Integer indexId;

	private Integer printerGroupId;

	private Integer userId;

	private Long createTime;

	private Long updateTime;

	private String imgUrl;

	public ItemCategory(){

	}


	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getId() {
		if (CommonUtil.isNull(id))
			return 0;
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemCategoryName() {
		if (CommonUtil.isNull(itemCategoryName))
			return "";
		return itemCategoryName;
	}

	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName == null ? null
				: itemCategoryName.trim();
	}

	public Integer getSuperCategoryId() {
		if (CommonUtil.isNull(superCategoryId))
			return 0;
		return superCategoryId;
	}

	public void setSuperCategoryId(Integer superCategoryId) {
		this.superCategoryId = superCategoryId;
	}

	public String getColor() {
		if (CommonUtil.isNull(color))
			return "";
		return color;
	}

	public void setColor(String color) {
		this.color = color == null ? null : color.trim();
	}

	public Integer getItemMainCategoryId() {
		if (CommonUtil.isNull(itemMainCategoryId))
			return 0;
		return itemMainCategoryId;
	}

	public void setItemMainCategoryId(Integer itemMainCategoryId) {
		this.itemMainCategoryId = itemMainCategoryId;
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

	public Integer getPrinterGroupId() {
		if (CommonUtil.isNull(printerGroupId))
			return 0;
		return printerGroupId;
	}

	public void setPrinterGroupId(Integer printerGroupId) {
		this.printerGroupId = printerGroupId;
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

//	@Override
//	public String toString() {
//		return "ItemCategory [id=" + id + ", itemCategoryName="
//				+ itemCategoryName + ", superCategoryId=" + superCategoryId
//				+ ", color=" + color + ", itemMainCategoryId="
//				+ itemMainCategoryId + ", restaurantId=" + restaurantId
//				+ ", isActive=" + isActive + ", indexId=" + indexId
//				+ ", printerGroupId=" + printerGroupId + ", userId=" + userId
//				+ ", createTime=" + createTime + ", updateTime=" + updateTime
//				+ "]";
//	}


	@Override
	public String toString() {
		return "ItemCategory{" +
				"id=" + id +
				", itemCategoryName='" + itemCategoryName + '\'' +
				", superCategoryId=" + superCategoryId +
				", color='" + color + '\'' +
				", itemMainCategoryId=" + itemMainCategoryId +
				", restaurantId=" + restaurantId +
				", isActive=" + isActive +
				", indexId=" + indexId +
				", printerGroupId=" + printerGroupId +
				", userId=" + userId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", imgUrl='" + imgUrl + '\'' +
				'}';
	}
}