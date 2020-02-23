package com.alfredbase.javabean;

import java.io.Serializable;

public class LoginResult implements Serializable {
	private static final long serialVersionUID = 3117292887028441345L;
	private String userKey;
	private String restaurantKey;

	public LoginResult(String userKey, String restaurantKey) {
		super();
		this.userKey = userKey;
		this.restaurantKey = restaurantKey;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getRestaurantKey() {
		return restaurantKey;
	}

	public void setRestaurantKey(String restaurantKey) {
		this.restaurantKey = restaurantKey;
	}

	@Override
	public String toString() {
		return "LoginResult [userKey=" + userKey + ", restaurantKey="
				+ restaurantKey + "]";
	}
}
