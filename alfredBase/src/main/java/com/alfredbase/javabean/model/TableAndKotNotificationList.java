package com.alfredbase.javabean.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableAndKotNotificationList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4006831988644187765L;
	private String tableName;
	private List<KotNotification> kotNotifications = new ArrayList<KotNotification>();
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<KotNotification> getKotNotifications() {
		return kotNotifications;
	}
	public void setKotNotifications(List<KotNotification> kotNotifications) {
		this.kotNotifications = kotNotifications;
	}
	@Override
	public String toString() {
		return "TableAndKotNotificationList [tableName=" + tableName
				+ ", kotNotifications=" + kotNotifications + "]";
	}
}
