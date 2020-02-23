package com.alfredbase.javabean.temporaryforapp;

import java.io.Serializable;

public class AppOrder implements Serializable {
    private Integer id;    //'主键id',
    private Integer orderNum;    //'订单编号'
    private Integer custId;    //'顾客id',
    private Integer restId;        //'餐厅id',
    private Integer revenueId;    //'收银中心id',
    private Integer sourceType;    //'订单来源类型(0外部第三方应用，1手机APP，2微信，3外卖)',
    private Integer tableId = 0;    // '桌子id',
    private Integer orderStatus;    //'订单状态(0未确认，1已确认, 已支付，2已下单到厨房，3已完成,)',
    private String subTotal;    //'订单总金额',
    private String taxAmount;    //'税收总金额',
    private String discountAmount;    //'打折总金额',
    private Integer discountType;    //'打折类型(0不打折、10主订单按照比率打折、11主订单直接减、12子订单打折)',
    private String total;    //'实收金额=订单总金额+税收总金额-打折总金额',
    private Integer orderCount;    //'订单数量',
    private long createTime;    //'创建时间',
    private long updateTime;    //'更新时间',
    private Integer tableType = 0; // 桌子状态,只用于本地,0默认状态,1桌子可以使用,-1桌子已经占用中 等待分配桌子。
    private String tableNo;
    private Integer bizType = 0; // 业务类型, 0点菜,1预点单
    private String orderRemark;
    private int eatType; // 0 堂吃, 1 打包, 2外卖
    private int payStatus;
    private int person;

    private String address;//外卖地址
    private String contact;
    private String mobile;  //收货人电话

    private long deliveryTime;

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderNo() {
        return orderNum;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNum = orderNo;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getRestId() {
        return restId;
    }

    public void setRestId(Integer restId) {
        this.restId = restId;
    }

    public Integer getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(Integer revenueId) {
        this.revenueId = revenueId;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
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

    public Integer getTableType() {
        return tableType;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public int getEatType() {
        return eatType;
    }

    public void setEatType(int eatType) {
        this.eatType = eatType;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "AppOrder{" +
                "id=" + id +
                ", orderNum=" + orderNum +
                ", custId=" + custId +
                ", restId=" + restId +
                ", revenueId=" + revenueId +
                ", sourceType=" + sourceType +
                ", tableId=" + tableId +
                ", orderStatus=" + orderStatus +
                ", subTotal='" + subTotal + '\'' +
                ", taxAmount='" + taxAmount + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", discountType=" + discountType +
                ", total='" + total + '\'' +
                ", orderCount=" + orderCount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", tableType=" + tableType +
                ", tableNo='" + tableNo + '\'' +
                ", bizType=" + bizType +
                ", orderRemark='" + orderRemark + '\'' +
                ", eatType=" + eatType +
                ", payStatus=" + payStatus +
                ", person=" + person +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", mobile='" + mobile + '\'' +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
