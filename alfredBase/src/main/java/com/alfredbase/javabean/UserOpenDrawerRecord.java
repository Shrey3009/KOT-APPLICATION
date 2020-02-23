package com.alfredbase.javabean;

/**
 * Created by Alex on 16/12/28.
 */

public class UserOpenDrawerRecord {
    private Integer id;
    private Integer restaurantId;
    private Integer revenueCenterId;
    private Integer sessionStatus;
    private Integer userId;
    private String userName;
    private Long  openTime;
    private Integer loginUserId;
    private int daySalesId;

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

    public Integer getRevenueCenterId() {
        return revenueCenterId;
    }

    public void setRevenueCenterId(Integer revenueCenterId) {
        this.revenueCenterId = revenueCenterId;
    }

    public Integer getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(Integer sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    public Integer getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Integer loginUserId) {
        this.loginUserId = loginUserId;
    }

    public int getDaySalesId() {
        return daySalesId;
    }

    public void setDaySalesId(int daySalesId) {
        this.daySalesId = daySalesId;
    }

    @Override
    public String toString() {
        return "UserOpenDrawerRecord{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", revenueCenterId=" + revenueCenterId +
                ", sessionStatus=" + sessionStatus +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", openTime=" + openTime +
                ", loginUserId=" + loginUserId +
                ", daySalesId=" + daySalesId +
                '}';
    }
}
