package com.alfredbase.javabean;

import java.io.Serializable;

/**
 * Created by Eric on 2018/7/4.
 */
public class ReportDayPayment implements Serializable{

    private Integer id;
    private Integer daySalesId;
    private Integer restaurantId;
    private String restaurantName;
    private Integer revenueId;
    private String revenueName;
    private Long businessDate;
    private Long paymentTypeId;
    private String paymentName;
    private Integer paymentQty;
    private String paymentAmount;
    private String overPaymentAmount = "0.00";
    private Long createTime;



    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDaySalesId() {
        return daySalesId;
    }

    public void setDaySalesId(Integer daySalesId) {
        this.daySalesId = daySalesId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Integer getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(Integer revenueId) {
        this.revenueId = revenueId;
    }

    public String getRevenueName() {
        return revenueName;
    }

    public void setRevenueName(String revenueName) {
        this.revenueName = revenueName;
    }

    public Long getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Long businessDate) {
        this.businessDate = businessDate;
    }

    public Long getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Integer getPaymentQty() {
        return paymentQty;
    }

    public void setPaymentQty(Integer paymentQty) {
        this.paymentQty = paymentQty;
    }

    public String getOverPaymentAmount() {
        return overPaymentAmount;
    }

    public void setOverPaymentAmount(String overPaymentAmount) {
        this.overPaymentAmount = overPaymentAmount;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public String toString() {
        return "ReportDayPayment{" +
                "id=" + id +
                ", daySalesId=" + daySalesId +
                ", restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", revenueId=" + revenueId +
                ", revenueName='" + revenueName + '\'' +
                ", businessDate=" + businessDate +
                ", paymentTypeId=" + paymentTypeId +
                ", paymentName='" + paymentName + '\'' +
                ", paymentQty=" + paymentQty +
                ", paymentAmount='" + paymentAmount + '\'' +
                ", overPaymentAmount='" + overPaymentAmount + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
