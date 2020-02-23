package com.alfredbase.javabean;

import com.alfredbase.utils.CommonUtil;

public class UserRestaurant {
	private Integer id;

	private Integer userId;

	private Integer restaurantId;

	private Integer revenueId;

	private Integer kitchenId;

	public Integer getId() {
		if (CommonUtil.isNull(id))
			return 0;
		return id;
	}

	public Integer getUserId() {
		if (CommonUtil.isNull(userId))
			return 0;
		return userId;
	}

	public Integer getRestaurantId() {
		if (CommonUtil.isNull(restaurantId))
			return 0;
		return restaurantId;
	}

	public Integer getRevenueId() {
		if (CommonUtil.isNull(revenueId))
			return 0;
		return revenueId;
	}

	public Integer getKitchenId() {
		if (CommonUtil.isNull(kitchenId))
			return 0;
		return kitchenId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public void setKitchenId(Integer kitchenId) {
		this.kitchenId = kitchenId;
	}

	@Override
	public String toString() {
		return "UserRestaurant [id=" + id + ", userId=" + userId
				+ ", restaurantId=" + restaurantId + ", revenueId=" + revenueId
				+ ", kitchenId=" + kitchenId + "]";
	}

}