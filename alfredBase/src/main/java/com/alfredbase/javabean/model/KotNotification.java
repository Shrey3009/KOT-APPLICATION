package com.alfredbase.javabean.model;

import java.io.Serializable;

public class KotNotification implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8798608576053289148L;

    private Integer id;
    private Integer orderDetailId;
    private Integer orderId;
    private Integer revenueCenterId;
    private String tableName;
    private String revenueCenterName;
    private String itemName;
    private Integer qty;
    private Integer session;
    private Integer status;    //	0为正常，-1为可删除
    private Integer unFinishQty;
    private Integer kotItemDetailId;
    private Integer kotItemNum;
    private String uniqueId;
    private String kotItemDetailUniqueId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getRevenueCenterId() {
        return revenueCenterId;
    }

    public void setRevenueCenterId(Integer revenueCenterId) {
        this.revenueCenterId = revenueCenterId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRevenueCenterName() {
        return revenueCenterName;
    }

    public void setRevenueCenterName(String revenueCenterName) {
        this.revenueCenterName = revenueCenterName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getSession() {
        return session;
    }

    public void setSession(Integer session) {
        this.session = session;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUnFinishQty() {
        return unFinishQty;
    }

    public void setUnFinishQty(Integer unFinishQty) {
        this.unFinishQty = unFinishQty;
    }

    public Integer getKotItemDetailId() {
        return kotItemDetailId;
    }

    public void setKotItemDetailId(Integer kotItemDetailId) {
        this.kotItemDetailId = kotItemDetailId;
    }

    public Integer getKotItemNum() {
        return kotItemNum;
    }

    public void setKotItemNum(Integer kotItemNum) {
        this.kotItemNum = kotItemNum;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getKotItemDetailUniqueId() {
        return kotItemDetailUniqueId;
    }

    public void setKotItemDetailUniqueId(String kotItemDetailUniqueId) {
        this.kotItemDetailUniqueId = kotItemDetailUniqueId;
    }

    @Override
    public String toString() {
        return "KotNotification [id=" + id + ", orderDetailId=" + orderDetailId
                + ", orderId=" + orderId + ", revenueCenterId="
                + revenueCenterId + ", tableName=" + tableName
                + ", revenueCenterName=" + revenueCenterName + ", itemName="
                + itemName + ", qty=" + qty + ", session=" + session
                + ", status=" + status + ", unFinishQty=" + unFinishQty
                + ", kotItemDetailId=" + kotItemDetailId + ", kotItemNum="
                + kotItemNum + "uniqueId=" + uniqueId + "kotItemDetailUniqueId=" + kotItemDetailUniqueId + "]";
    }

}
