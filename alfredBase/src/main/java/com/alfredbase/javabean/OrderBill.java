package com.alfredbase.javabean;

import java.io.Serializable;

public class OrderBill implements Serializable {

    private Integer id;
    private Integer billNo;
    private Integer orderId;
    private Integer orderSplitId;
    /**
     * type是bill的类型 （0未拆单，1已拆单）
     */
    private Integer type;
    private Integer restaurantId;
    private Integer revenueId;
    private Integer userId;
    private Long createTime;
    private Long updateTime;
    private Long printTime;

    public OrderBill() {
        super();
    }

    public OrderBill(Integer id, Integer billNo, Integer orderId,
                     Integer orderSplitId, Integer type, Integer restaurantId,
                     Integer revenueId, Integer userId, Long createTime,
                     Long updateTime) {
        super();
        this.id = id;
        this.billNo = billNo;
        this.orderId = orderId;
        this.orderSplitId = orderSplitId;
        this.type = type;
        this.restaurantId = restaurantId;
        this.revenueId = revenueId;
        this.userId = userId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillNo() {
        return billNo;
    }

    public void setBillNo(Integer billNo) {
        this.billNo = billNo;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderSplitId() {
        return orderSplitId;
    }

    public void setOrderSplitId(Integer orderSplitId) {
        this.orderSplitId = orderSplitId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(Integer revenueId) {
        this.revenueId = revenueId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Long printTime) {
        this.printTime = printTime;
    }
}
