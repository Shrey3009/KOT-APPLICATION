package com.alfredbase.javabean.temporaryforapp;

/**
 * Created by Alex on 16/7/1.
 */

public class AppOrderDetailTax {
    private Integer id;     //'主键id',
    private Integer orderId;    //'主订单id',
    private Integer orderDetailId;  //'订单详情id',
    private Integer taxId;  //'收税id',
    private String taxName;     //'税收名称',
    private String taxPercentage;   //'税收比例',
    private String taxPrice;    //'税收金额',
    private Integer taxType;    //'税收类型(0消费税，1服务税)',
    private long createTime;    //'创建时间',
    private long updateTime;    //'更新时间'

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(String taxPrice) {
        this.taxPrice = taxPrice;
    }

    public Integer getTaxType() {
        return taxType;
    }

    public void setTaxType(Integer taxType) {
        this.taxType = taxType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AppOrderDetailTax{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", orderDetailId=" + orderDetailId +
                ", taxId=" + taxId +
                ", taxName='" + taxName + '\'' +
                ", taxPercentage='" + taxPercentage + '\'' +
                ", taxPrice='" + taxPrice + '\'' +
                ", taxType=" + taxType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
