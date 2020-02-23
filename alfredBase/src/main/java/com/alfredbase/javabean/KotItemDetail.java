package com.alfredbase.javabean;

import java.io.Serializable;

public class KotItemDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6754950186409258464L;

    private Integer id;

    private Integer restaurantId;

    private Integer revenueId;

    private Integer orderId;

    private Integer orderDetailId;

    private Integer printerGroupId;

    private Integer kotSummaryId;

    private String itemName;

    private Integer itemNum;
    //	当前 提交pos机的菜的数量
    private Integer finishQty;

    private Integer sessionStatus;
    /**
     * 0未发送、1待完成、2更新、3已完成、4已退单、-1已删除
     */
    private Integer kotStatus;

    private String specialInstractions;

    private Integer version;

    private Long createTime;

    private Long updateTime;
    //	剩余没有做的菜的数量
    private Integer unFinishQty;

    private Integer categoryId;

    /**
     * 是否外带(0不外带、1外带)
     */
    private int isTakeAway;

    private int fireStatus;

    /**
     * (0未叫号、1已叫号)
     */
    private int callType;

    private long expectedTime;

    private long startTime;

    private long endTime;

    private int itemType;

    private Integer itemId;

    private String uniqueId;

    private String kotSummaryUniqueId;

    public boolean isChecklist;

    public KotItemDetail() {
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

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

    public Integer getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(Integer revenueId) {
        this.revenueId = revenueId;
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

    public Integer getPrinterGroupId() {
        return printerGroupId;
    }

    public void setPrinterGroupId(Integer printerGroupId) {
        if (printerGroupId == null)
            printerGroupId = 0;
        this.printerGroupId = printerGroupId;
    }

    public Integer getKotSummaryId() {
        return kotSummaryId;
    }

    public void setKotSummaryId(Integer kotSummaryId) {
        this.kotSummaryId = kotSummaryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public Integer getFinishQty() {
        return finishQty;
    }

    public void setFinishQty(Integer finishQty) {
        this.finishQty = finishQty;
    }

    public Integer getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(Integer sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Integer getKotStatus() {
        return kotStatus;
    }

    public void setKotStatus(Integer kotStatus) {
        this.kotStatus = kotStatus;
    }

    public String getSpecialInstractions() {
        return specialInstractions;
    }

    public void setSpecialInstractions(String specialInstractions) {
        this.specialInstractions = specialInstractions;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public Integer getUnFinishQty() {
        return unFinishQty;
    }

    public void setUnFinishQty(Integer unFinishQty) {
        this.unFinishQty = unFinishQty;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsTakeAway() {
        return isTakeAway;
    }

    public void setIsTakeAway(int isTakeAway) {
        this.isTakeAway = isTakeAway;
    }

    public int getFireStatus() {
        return fireStatus;
    }

    public void setFireStatus(int fireStatus) {
        this.fireStatus = fireStatus;
    }

    public long getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(long expectedTime) {
        this.expectedTime = expectedTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getKotSummaryUniqueId() {
        return kotSummaryUniqueId;
    }

    public void setKotSummaryUniqueId(String kotSummaryUniqueId) {
        this.kotSummaryUniqueId = kotSummaryUniqueId;
    }

    @Override
    public String toString() {
        return "KotItemDetail{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", revenueId=" + revenueId +
                ", orderId=" + orderId +
                ", orderDetailId=" + orderDetailId +
                ", printerGroupId=" + printerGroupId +
                ", kotSummaryId=" + kotSummaryId +
                ", itemName='" + itemName + '\'' +
                ", itemNum=" + itemNum +
                ", finishQty=" + finishQty +
                ", sessionStatus=" + sessionStatus +
                ", kotStatus=" + kotStatus +
                ", specialInstractions='" + specialInstractions + '\'' +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", unFinishQty=" + unFinishQty +
                ", categoryId=" + categoryId +
                ", isTakeAway=" + isTakeAway +
                ", fireStatus=" + fireStatus +
                ", callType=" + callType +
                ", expectedTime=" + expectedTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", itemType=" + itemType +
                ", itemId=" + itemId +
                ", isChecklist=" + isChecklist +
                ", uniqueId='" + uniqueId + '\'' +
                ", kotSummaryUniqueId='" + kotSummaryUniqueId + '\'' +
                '}';
    }
}