package com.alfredbase.javabean;

import java.io.Serializable;

public class Tables implements Serializable{
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 8935847982682932114L;
//
//	private Integer id;
//
//	private Integer restaurantId;
//
//	private Integer revenueId;
//
//	private Integer placesId;
//
//	private String tableName;
//
//	/**
//	 * 桌子人数
//	 */
//	private Integer tablePacks;
//	/**
//	 * 是否可用(-1删除，0禁用，1正常)
//	 */
//	private Integer isActive;
//	// 桌子状态 0 空的、 1 占用、 2 正在结账
//	private Integer tableStatus = 0;
//
//	// add: calculate the total orders that table has currently.
//	private Integer orders = 0;
//
//	public Tables() {
//	}
//
//	public Tables(Integer id, Integer restaurantId, Integer revenueId,
//			Integer placesId, String tableName, Integer tablePacks,
//			Integer isActive, Integer tableStatus, Integer orders) {
//		super();
//		this.id = id;
//		this.restaurantId = restaurantId;
//		this.revenueId = revenueId;
//		this.placesId = placesId;
//		this.tableName = tableName;
//		this.tablePacks = tablePacks;
//		this.isActive = isActive;
//		this.tableStatus = tableStatus;
//		this.orders = orders;
//	}
//
//
//
//	public Integer getId() {
//		if (CommonUtil.isNull(id))
//			return 0;
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public Integer getRestaurantId() {
//		if (CommonUtil.isNull(restaurantId))
//			return 0;
//		return restaurantId;
//	}
//
//	public void setRestaurantId(Integer restaurantId) {
//		this.restaurantId = restaurantId;
//	}
//
//	public Integer getRevenueId() {
//		if (CommonUtil.isNull(revenueId))
//			return 0;
//		return revenueId;
//	}
//
//	public void setRevenueId(Integer revenueId) {
//		this.revenueId = revenueId;
//	}
//
//	public Integer getPlacesId() {
//		if (CommonUtil.isNull(placesId))
//			return 0;
//		return placesId;
//	}
//
//	public void setPlacesId(Integer placesId) {
//		this.placesId = placesId;
//	}
//
//	public String getTableName() {
//		if (CommonUtil.isNull(tableName))
//			return "";
//		return tableName;
//	}
//
//	public void setTableName(String tableName) {
//		this.tableName = tableName == null ? null : tableName.trim();
//	}
//
//	public Integer getTablePacks() {
//		if (CommonUtil.isNull(tablePacks))
//			return 0;
//		return tablePacks;
//	}
//
//	public void setTablePacks(Integer tablePacks) {
//		this.tablePacks = tablePacks;
//	}
//
//	public Integer getIsActive() {
//		if (CommonUtil.isNull(isActive))
//			return 0;
//		return isActive;
//	}
//
//	public void setIsActive(Integer isActive) {
//		this.isActive = isActive;
//	}
//
//	public Integer getTableStatus() {
//		if (CommonUtil.isNull(tableStatus))
//			return 0;
//		return tableStatus;
//	}
//
//	public void setTableStatus(Integer tableStatus) {
//		this.tableStatus = tableStatus;
//	}
//
//	@Override
//	public String toString() {
//		return "Tables [id=" + id + ", restaurantId=" + restaurantId
//				+ ", revenueId=" + revenueId + ", placesId=" + placesId
//				+ ", tableName=" + tableName + ", tablePacks=" + tablePacks
//				+ ", isActive=" + isActive + ", tableStatus=" + tableStatus
//				+ ", orders=" + orders + "]";
//	}
//
//	public Integer getOrders() {
//		return orders;
//	}
//
//	public void setOrders(Integer orders) {
//		this.orders = orders;
//	}

}