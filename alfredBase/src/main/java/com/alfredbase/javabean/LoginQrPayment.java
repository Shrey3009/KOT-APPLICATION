package com.alfredbase.javabean;

public class LoginQrPayment {
    private String name;
    private String restaurantKey;
    private int resultCode; //"resultCode": 1 is success || -2001 is error
    private String userKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }

    public void setRestaurantKey(String restaurantKey) {
        this.restaurantKey = restaurantKey;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
