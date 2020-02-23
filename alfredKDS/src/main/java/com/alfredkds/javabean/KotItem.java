package com.alfredkds.javabean;

import java.io.Serializable;

public class KotItem implements Serializable {

    private int id;
    private String itemDetailName;
    private String itemDetail;
    private String itemModName;
    private String tableName;
    private Integer callType;
    private Integer kotStatus;
    private Long createTime;
    private Long updateTime;
    private Integer orderNo;
    private Integer summaryId;
    private Integer qty;
    private Integer itemDetailId;
    private String numTag;
    private Integer revenueCenterIndex;
    private boolean isPlaceOrder;
    private int itemId;
    private String kotItemDetailUniqueId;

    public boolean isPlaceOrder() {
        return isPlaceOrder;
    }

    public void setPlaceOrder(boolean placeOrder) {
        isPlaceOrder = placeOrder;
    }

    public Integer getRevenueCenterIndex() {
        return revenueCenterIndex;
    }

    public void setRevenueCenterIndex(Integer revenueCenterIndex) {
        this.revenueCenterIndex = revenueCenterIndex;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getItemDetailId() {
        return itemDetailId;
    }

    public void setItemDetailId(Integer itemDetailId) {
        this.itemDetailId = itemDetailId;
    }

    public Integer getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(Integer summaryId) {
        this.summaryId = summaryId;
    }

    public Integer getCallType() {
        return callType;
    }

    public void setCallType(Integer callType) {
        this.callType = callType;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemDetailName() {
        return itemDetailName;
    }

    public void setItemDetailName(String itemDetailName) {
        this.itemDetailName = itemDetailName;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(String itemDetail) {
        this.itemDetail = itemDetail;
    }

    public String getItemModName() {
        return itemModName;
    }

    public void setItemModName(String itemModName) {
        this.itemModName = itemModName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public Integer getKotStatus() {
        return kotStatus;
    }

    public void setKotStatus(Integer kotStatus) {
        this.kotStatus = kotStatus;
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

    public String getNumTag() {
        return numTag;
    }

    public void setNumTag(String numTag) {
        this.numTag = numTag;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getKotItemDetailUniqueId() {
        return kotItemDetailUniqueId;
    }

    public void setKotItemDetailUniqueId(String kotItemDetailUniqueId) {
        this.kotItemDetailUniqueId = kotItemDetailUniqueId;
    }

    @Override
    public String toString() {
        return "KotItem{" +
                "id=" + id +
                ", itemDetailName='" + itemDetailName + '\'' +
                ", itemDetail='" + itemDetail + '\'' +
                ", itemModName='" + itemModName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", callType=" + callType +
                ", kotStatus=" + kotStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderNo=" + orderNo +
                ", summaryId=" + summaryId +
                ", qty=" + qty +
                ", itemDetailId=" + itemDetailId +
                ", numTag='" + numTag + '\'' +
                ", revenueCenterIndex=" + revenueCenterIndex +
                ", isPlaceOrder=" + isPlaceOrder +
                ", itemId=" + itemId +
                ", kotItemDetailUniqueId=" + kotItemDetailUniqueId +
                '}';
    }
}
