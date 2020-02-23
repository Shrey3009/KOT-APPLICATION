package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

public class PlaceInfo {
	private Integer posId;

	private String placeName;

	private String placeDescription;

	private Integer restaurantId;

	private Integer revenueId;

	private String unionId;
	/**
	 * 是否可用(-1删除，0禁用，1正常)
	 */
	private Integer isActive;


	private Integer isKiosk = 0;

	public PlaceInfo() {
	}

	public PlaceInfo(Integer posId, String placeName, String placeDescription,
					 Integer restaurantId, Integer revenueId, Integer isActive) {
		super();
		this.posId = posId;
		this.placeName = placeName;
		this.placeDescription = placeDescription;
		this.restaurantId = restaurantId;
		this.revenueId = revenueId;
		this.isActive = isActive;
	}

	public Integer getId() {
		if (CommonUtil.isNull(posId))
			return 0;
		return posId;
	}

	public void setId(Integer posId) {
		this.posId = posId;
	}

	public String getPlaceName() {
		if (CommonUtil.isNull(placeName))
			return "";
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName == null ? null : placeName.trim();
	}

	public String getPlaceDescription() {
		if (CommonUtil.isNull(placeDescription))
			return "";
		return placeDescription;
	}

	public void setPlaceDescription(String placeDescription) {
		this.placeDescription = placeDescription == null ? null
				: placeDescription.trim();
	}

	public Integer getRestaurantId() {
		if (CommonUtil.isNull(restaurantId))
			return 0;
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Integer getRevenueId() {
		if (CommonUtil.isNull(revenueId))
			return 0;
		return revenueId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public Integer getIsActive() {
		if (CommonUtil.isNull(isActive))
			return 0;
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsKiosk() {
		return isKiosk;
	}

	public void setIsKiosk(Integer isKiosk) {
		this.isKiosk = isKiosk;
	}

	@Override
	public String toString() {
		return "PlaceInfo{" +
				"posId=" + posId +
				", placeName='" + placeName + '\'' +
				", placeDescription='" + placeDescription + '\'' +
				", restaurantId=" + restaurantId +
				", revenueId=" + revenueId +
				", unionId='" + unionId + '\'' +
				", isActive=" + isActive +
				", isKiosk=" + isKiosk +
				'}';
	}

}